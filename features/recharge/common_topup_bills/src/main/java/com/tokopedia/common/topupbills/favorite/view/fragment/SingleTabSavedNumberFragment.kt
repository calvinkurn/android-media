package com.tokopedia.common.topupbills.favorite.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.databinding.FragmentPersoSingleTabSavedNumberBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SingleTabSavedNumberFragment: BaseDaggerFragment() {

    private lateinit var clientNumberType: String
    private lateinit var dgCategoryIds: ArrayList<String>
    private lateinit var dgOperatorIds: ArrayList<String>
    private var currentCategoryName = ""
    private var number: String = ""
    private var loyaltyStatus: String = ""
    private var operatorList: HashMap<String, TelcoAttributesOperator> = hashMapOf()

    private var binding: FragmentPersoSingleTabSavedNumberBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val savedNumberViewModel by lazy {
        viewModelFragmentProvider.get(TopupBillsSavedNumberViewModel::class.java) }

    @Suppress("UNCHECKED_CAST")
    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, "")
            number = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            dgCategoryIds = arguments.getStringArrayList(ARG_PARAM_DG_CATEGORY_IDS) ?: arrayListOf()
            dgOperatorIds = arguments.getStringArrayList(ARG_PARAM_DG_OPERATOR_IDS) ?: arrayListOf()
            currentCategoryName = arguments.getString(ARG_PARAM_CATEGORY_NAME, "")
            loyaltyStatus = arguments.getString(ARG_PARAM_LOYALTY_STATUS, "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    override fun getScreenName(): String {
        return SingleTabSavedNumberFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersoSingleTabSavedNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChildView()
        initListener()
        observeData()
    }

    private fun initChildView() {
        val favoriteNumberFragment = TopupBillsPersoFavoriteNumberFragment.newInstance(
            clientNumberType,
            number,
            currentCategoryName,
            dgCategoryIds,
            dgOperatorIds,
            loyaltyStatus
        )
        childFragmentManager
            .beginTransaction()
            .replace(R.id.common_topup_bills_single_container, favoriteNumberFragment)
            .commit()
    }

    private fun initListener() {
        binding?.run {
            commonTopupBillsSingleSavedNumSearchbar.searchBarTextField.addTextChangedListener(
                getSearchTextWatcher
            )
        }
    }

    private fun observeData() {
        savedNumberViewModel.clueVisibility.observe(viewLifecycleOwner, { isVisible ->
            if (isVisible) {
                binding?.commonTopupBillsSingleFavoriteNumberClue?.show()
            } else {
                binding?.commonTopupBillsSingleFavoriteNumberClue?.hide()
            }
        })

        savedNumberViewModel.enableSearchBar.observe(viewLifecycleOwner, { isEnable ->
            if (isEnable) {
                binding?.commonTopupBillsSingleSavedNumSearchbar?.show()
            } else {
                binding?.commonTopupBillsSingleSavedNumSearchbar?.hide()
            }
        })
    }

    private fun saveTelcoOperator(rechargeCatalogPrefixSelect: RechargeCatalogPrefixSelect) {
        val operatorList = HashMap<String, TelcoAttributesOperator>()

        rechargeCatalogPrefixSelect.prefixes.forEach {
            if (!operatorList.containsKey(it.operator.id)) {
                operatorList[it.operator.id] = it.operator.attributes
            }
        }

        this.operatorList = operatorList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.commonTopupBillsSingleSavedNumSearchbar?.searchBarTextField?.removeTextChangedListener(
            getSearchTextWatcher
        )
    }

    private val getSearchTextWatcher = object : android.text.TextWatcher {
        override fun afterTextChanged(text: android.text.Editable?) {
            text?.let {
                savedNumberViewModel.setSearchKeyword(text.toString())
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    companion object {
        fun newInstance(
            clientNumberType: String, number: String,
            categoryName: String, digitalCategoryIds: ArrayList<String>,
            digitalOperatorIds: ArrayList<String>, loyaltyStatus: String
        ): Fragment {

            val fragment = SingleTabSavedNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, number)
            bundle.putString(ARG_PARAM_CATEGORY_NAME, categoryName.lowercase())
            bundle.putString(ARG_PARAM_LOYALTY_STATUS, loyaltyStatus)
            bundle.putStringArrayList(ARG_PARAM_DG_CATEGORY_IDS, digitalCategoryIds)
            bundle.putStringArrayList(ARG_PARAM_DG_OPERATOR_IDS, digitalOperatorIds)
            fragment.arguments = bundle
            return fragment
        }

        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER_TYPE = "ARG_PARAM_EXTRA_CLIENT_NUMBER"
        const val ARG_PARAM_DG_CATEGORY_IDS = "ARG_PARAM_DG_CATEGORY_IDS"
        const val ARG_PARAM_DG_OPERATOR_IDS = "ARG_PARAM_DG_OPERATOR_IDS"
        const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"
        const val ARG_PARAM_LOYALTY_STATUS = "ARG_PARAM_LOYALTY_STATUS"
    }
}