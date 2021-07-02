package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberModData
import com.tokopedia.common.topupbills.data.UpdateFavoriteDetail
import com.tokopedia.common.topupbills.databinding.FragmentFavoriteNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.utils.covertContactUriToContactData
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsFavoriteNumberListAdapter
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberMenuBottomSheet
import com.tokopedia.common.topupbills.view.bottomsheet.FavoriteNumberModifyBottomSheet
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberMenuListener
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberModifyListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactoryImpl
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.util.ArrayList
import javax.inject.Inject
import kotlin.properties.Delegates

class TopupBillsFavoriteNumberFragment :
        BaseDaggerFragment(),
        OnFavoriteNumberClickListener,
        FavoriteNumberMenuListener,
        FavoriteNumberEmptyStateListener,
        FavoriteNumberModifyListener
{
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }

    private lateinit var numberListAdapter: TopupBillsFavoriteNumberListAdapter
    private lateinit var clientNumbers: List<TopupBillsSeamlessFavNumberItem>
    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>

    private var number: String = ""
    protected lateinit var inputNumberActionType: InputNumberActionType

    private var binding: FragmentFavoriteNumberBinding? = null

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return TopupBillsFavoriteNumberFragment::class.java.simpleName
    }

    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            number = arguments.getString(ARG_PARAM_EXTRA_NUMBER, "")
            clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST) ?: listOf()
            dgCategoryIds = arguments.getStringArrayList(ARG_PARAM_DG_CATEGORY_IDS) ?: arrayListOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    fun initView() {
        setClientNumberInputType()
        if (number.isNotEmpty()) {
            binding?.run {
                commonTopupbillsSearchNumberInputView.searchBarTextField.setText(number)
                commonTopupbillsSearchNumberInputView.searchBarTextField.setSelection(number.length)
                commonTopupbillsSearchNumberInputView.searchBarIcon.clearAnimation()
                commonTopupbillsSearchNumberInputView.searchBarIcon.post {
                    commonTopupbillsSearchNumberInputView.searchBarIcon.animate().scaleX(1f).scaleY(1f).start()
                }
            }
        }
        val typeFactory = FavoriteNumberTypeFactoryImpl(this, this)

        binding?.commonTopupbillsSearchNumberInputView?.run {
            searchBarTextField.addTextChangedListener(getSearchTextWatcher)
            searchBarTextField.setOnEditorActionListener(getSearchNumberListener)
            searchBarTextField.onFocusChangeListener = getFocusChangeListener
            clearListener = { onSearchReset() }
            searchBarTextField.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        binding?.commonTopupbillsFavoriteNumberClue?.run {
            if (clientNumbers.isNullOrEmpty()) hide() else show()
        }

        binding?.commonTopupbillsSearchNumberContactPicker?.setOnClickListener {
            inputNumberActionType = InputNumberActionType.CONTACT
            navigateContact()
        }

        if (clientNumbers.isNotEmpty()) {
            numberListAdapter = TopupBillsFavoriteNumberListAdapter(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(clientNumbers), typeFactory)
        } else {
            numberListAdapter = TopupBillsFavoriteNumberListAdapter(
                    listOf(TopupBillsFavNumberNotFoundDataView()), typeFactory)
        }

        binding?.commonTopupbillsFavoriteNumberRv?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = numberListAdapter
        }
    }

    var INDEX = 0
    private fun observeData() {
        topUpBillsViewModel.seamlessFavNumberUpdateData.observe(viewLifecycleOwner, Observer {
            val dummy = UpdateFavoriteDetail(
                    categoryID = 0,
                    clientNumber = "123123",
                    label = "Misael Baru",
                    lastOrderDate = "",
                    lastUpdated = "",
                    operatorID = 0,
                    productID = 0,
                    subscribed = true,
                    totalTransaction = 0,
                    wishlist = true
            )
            when (it) {
                is Success -> onSuccessUpdateClientName()
                is Fail -> onFailedUpdateClientName()
            }
//            when (it) {
//                is Success -> onSuccessUpdateClientName(dummy, it.data.second)
//                is Fail -> onSuccessUpdateClientName(dummy, INDEX++)
//            }
        })

        topUpBillsViewModel.seamlessFavNumberData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetFavoriteNumber(it.data.favoriteNumbers)
                is Fail -> onFailedGetFavoriteNumber()
            }
        })
    }

    private fun onSuccessGetFavoriteNumber(newClientNumbers: List<TopupBillsSeamlessFavNumberItem>) {
        clientNumbers = newClientNumbers
        numberListAdapter.setNumbers(CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(
                clientNumbers
        ))
    }

    private fun onFailedGetFavoriteNumber() {
        view?.let {
            Toaster.build(it, "anjay gagal", Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private val getFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) inputNumberActionType = InputNumberActionType.MANUAL
    }

    private val getSearchNumberListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchSubmitted(textView.text.toString())
                return true
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchDone(textView.text.toString())
                return true
            }
            return false
        }
    }

    private val getSearchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            text?.let { filterData(text.toString()) }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    private fun setClientNumberInputType() {
        binding?.commonTopupbillsSearchNumberInputView
                ?.searchBarTextField?.inputType = when (clientNumberType.toLowerCase()) {
            ClientNumberType.TYPE_INPUT_TEL -> InputType.TYPE_CLASS_PHONE
            ClientNumberType.TYPE_INPUT_NUMERIC -> InputType.TYPE_CLASS_NUMBER
            ClientNumberType.TYPE_INPUT_ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsSeamlessFavNumberItem>()

        searchClientNumbers.addAll(clientNumbers.filter {
            it.clientNumber.contains(query)
        })

        if (searchClientNumbers.isNotEmpty()) {
            numberListAdapter.setNumbers(
                    CommonTopupBillsDataMapper.mapSeamlessFavNumberItemToDataView(searchClientNumbers)
            )
        } else {
            numberListAdapter.setEmptyState(listOf(TopupBillsFavNumberEmptyDataView()))
        }
    }


    private fun findNumber(number: String, clientNumbers: List<TopupBillsSeamlessFavNumberItem>): TopupBillsSeamlessFavNumberItem? {
        var foundClientNumber: TopupBillsSeamlessFavNumberItem? = null
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.equals(number, ignoreCase = true)) {
                foundClientNumber = orderClientNumber
                break
            }
        }
        return foundClientNumber
    }

    fun onSearchSubmitted(text: String?) {
        //do nothing
    }

    fun onSearchDone(text: String) {
        navigateToPDP(InputNumberActionType.MANUAL)
    }

    fun onSearchReset() {
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.setText("")
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onFavoriteNumberClick(clientNumber: TopupBillsSeamlessFavNumberItem) {
        navigateToPDP(InputNumberActionType.FAVORITE, clientNumber)
    }

    override fun onContinueClicked() {
        navigateToPDP(InputNumberActionType.MANUAL)
    }

    private fun navigateToPDP(
            inputNumberActionType: InputNumberActionType,
            clientNumber: TopupBillsSeamlessFavNumberItem? = null
    ) {
        activity?.run {
            val intent = Intent()
            val searchedClientNumber: TopupBillsSeamlessFavNumberItem = clientNumber
                    ?: TopupBillsSeamlessFavNumberItem(
                            clientNumber = binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.text.toString()
                    )

            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, searchedClientNumber)
            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun navigateContact() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.checkPermission(it,
                        PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                openContactPicker()
                            }
                        })
            }
        } else {
            openContactPicker()
        }
    }

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            view?.let {
                Toaster.build(it, getString(R.string.common_topup_contact_not_found), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    private fun onSuccessUpdateClientName() {
        topUpBillsViewModel.getSeamlessFavoriteNumbers(
                CommonTopupBillsGqlQuery.rechargeFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberParams(dgCategoryIds)
        )

        view?.let {
            Toaster.build(it, "Oke, nama berhasil diubah.", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun onFailedUpdateClientName() {
        view?.let {
            Toaster.build(it, "Maaf, belum berhasil diubah. Coba lagi, ya.", Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val bottomSheet = FavoriteNumberMenuBottomSheet.newInstance(
                favNumberItem, this)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onChangeNameMenuClicked(favNumberItem: TopupBillsSeamlessFavNumberItem) {
        val bottomSheet = FavoriteNumberModifyBottomSheet.newInstance(favNumberItem, this)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onDeleteContactClicked() {
        // TODO: [Misael] delete contact
    }

    override fun onChangeName(newName: String, favNumberItem: TopupBillsSeamlessFavNumberItem) {
        topUpBillsViewModel.updateSeamlessFavoriteNumber(
                CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
                topUpBillsViewModel.createSeamlessFavoriteNumberUpdateParams(
                        categoryId = favNumberItem.categoryId,
                        productId = favNumberItem.productId,
                        clientNumber = favNumberItem.clientNumber,
                        totalTransaction = 0,
                        label = newName
                )
        )

    }

    enum class InputNumberActionType {
        MANUAL, CONTACT, FAVORITE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        data.data?.run {
                            val contact = this.covertContactUriToContactData(it.contentResolver)
                            val clientNumber = TopupBillsSeamlessFavNumberItem(
                                    clientName = contact.givenName,
                                    clientNumber = contact.contactNumber)
                            navigateToPDP(InputNumberActionType.CONTACT, clientNumber)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_CONTACT_PICKER = 75

        const val ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST"
        const val ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"

        fun newInstance(clientNumberType: String, number: String,
                        numberList: List<TopupBillsSeamlessFavNumberItem>,
                        digitalCategoryIds: ArrayList<String>
        ): Fragment {
            val fragment = TopupBillsFavoriteNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }
}
