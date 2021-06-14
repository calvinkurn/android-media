package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.db.District
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.ANA_POSITIVE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.android.synthetic.main.form_add_new_address_default_item.*
import javax.inject.Inject

class AddressFormFragment : BaseDaggerFragment(), LabelAlamatChipsAdapter.ActionListener {

    private var saveDataModel: SaveAddressDataModel? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var labelAlamatList: Array<String> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager

    private var binding by autoCleared<FragmentAddressFormBinding>()

    private val viewModel: AddressFormViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddressFormViewModel::class.java)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getScreenName(): String  = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
            currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()
        initObserver()
    }

    private fun prepareData() {
        viewModel.getDistrictDetail(saveDataModel?.districtId.toString())
    }

    private fun initObserver() {
        viewModel.districtDetail.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    prepareLayout(it.data.district[0])
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        })

        viewModel.saveAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        saveDataModel?.id = it.data.addrId
                        onSuccessAddAddress()
                    }
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem) {
        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(this)
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        staticDimen8dp = context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        setOnTouchLabelAddress(ANA_POSITIVE)
        setupRvLabelAlamatChips()

        binding.run {
            cardAddress.addressDistrict.text = "${data.districtName}, ${data.cityName}, ${data.provinceName}"

            formAddress.etLabel.textFieldInput.setText("Rumah")

            formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
            formAccount.etNomorHp.setFirstIcon(com.tokopedia.iconunify.R.drawable.iconunify_contact)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                Toast.makeText(context, "muncul", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnSaveAddress.setOnClickListener {
            doSaveAddress()
        }
    }

    private fun setupRvLabelAlamatChips() {
        binding.formAddress.rvLabelAlamatChips.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = labelAlamatChipsLayoutManager
            adapter = labelAlamatChipsAdapter
        }
    }

    private fun setOnTouchLabelAddress(type: String) {
        binding.formAddress.etLabel.textFieldInput.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat(type)
                } else {
                    binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
                }
            }
            setOnClickListener {
                eventShowListLabelAlamat(type)
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                               after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    val filterList = labelAlamatList.filter {
                        it.contains("$s", true)
                    }
                    labelAlamatChipsAdapter.submitList(filterList)
                }
            })
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
        }
    }

    private fun eventShowListLabelAlamat(type: String) {
        showLabelAlamatList()
    }

    private fun showLabelAlamatList() {
        val res: Resources = resources
        labelAlamatList = res.getStringArray(R.array.labelAlamatList)

        binding.formAddress.rvLabelAlamatChips.visibility = View.VISIBLE
        ViewCompat.setLayoutDirection(binding.formAddress.rvLabelAlamatChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let { viewModel.saveAddress(it) }
    }

    private fun setSaveAddressDataModel() {
        saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text} (${binding.formAddress.etCourierNote.textFieldInput.text})"
        saveDataModel?.address2 = "$currentLat,$currentLong"
        saveDataModel?.addressName =  binding.formAddress.etLabel.textFieldInput.text.toString()
        saveDataModel?.receiverName = binding.formAccount.etNamaPenerima.textFieldInput.text.toString()
        saveDataModel?.phone = binding.formAccount.etNomorHp.textFieldInput.text.toString()
    }

    private fun onSuccessAddAddress() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveDataModel)
            })
            finish()
        }
    }

    companion object {

        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRES_NEW"

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                }
            }
        }
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
        binding.formAddress.etLabel.textFieldInput.run {
            setText(labelAlamat)
            setSelection(binding.formAddress.etLabel.textFieldInput.text.length)
        }
    }

}