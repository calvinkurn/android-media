package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChooseAddressBottomSheet(private val listener: ChooseAddressBottomSheetListener): BottomSheetUnify(), HasComponent<ChooseAddressComponent>, AddressListItemAdapter.AddressListItemAdapterListener{

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChooseAddressViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ChooseAddressViewModel::class.java]
    }

    private val adapter = AddressListItemAdapter(this)
    private var chooseAddressLayout: ConstraintLayout? = null
    private var noAddressLayout: ConstraintLayout? = null
    private var loginLayout: ConstraintLayout? = null
    private var buttonLogin: IconUnify? = null
    private var buttonAddAddress: IconUnify? = null
    private var buttonSnippetLocation: IconUnify? = null
    private var addressList: RecyclerView? = null

    private var fm: FragmentManager? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()
    }

    /*on activity result here*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initLayout() {
        val view = View.inflate(context, R.layout.bottomsheet_choose_address, null)
        setupView(view)
        setChild(view)
        setTitle("Mau kirim belanjaan ke mana?")
        setCloseClickListener { this.dismiss() }
    }

    private fun setupView(child: View) {
        chooseAddressPref = ChooseAddressSharePref(context)
        chooseAddressLayout = child.findViewById(R.id.choose_address_layout)
        noAddressLayout = child.findViewById(R.id.no_address_layout)
        loginLayout = child.findViewById(R.id.login_layout)
        buttonLogin = child.findViewById(R.id.btn_chevron_login)
        buttonAddAddress = child.findViewById(R.id.btn_chevron_add)
        buttonSnippetLocation = child.findViewById(R.id.btn_chevron_snippet)
        addressList = child.findViewById(R.id.rv_address_card)

        addressList?.adapter = adapter
        addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        viewModel.getChosenAddressList()
    }

    private fun initObserver() {
        viewModel.chosenAddressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    adapter.updateData(it.data)
                    setViewState()
                }
            }
        })


        viewModel.setChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val data = it.data
                    val localData = ChooseAddressUtils.setLocalizingAddressData(
                            addressId = data.addressId.toString(),
                            cityId = data.cityId.toString(),
                            districtId = data.districtId.toString(),
                            lat = data.latitude,
                            long = data.longitude,
                            label = data.addressName
                    )
                    chooseAddressPref?.setLocalCache(localData)
                    listener.onAddressDataChanged()
                    this.dismiss()
                }

                is Fail -> {
                    listener.onLocalizingAddressServerDown()
                    showError(it.throwable)
                }

            }
        })
    }

    private fun setViewState() {
        if (!userSession.isLoggedIn) {
            chooseAddressLayout?.gone()
            noAddressLayout?.gone()
            loginLayout?.visible()
        } else {
            if (adapter.addressList.isEmpty()) {
                chooseAddressLayout?.gone()
                noAddressLayout?.visible()
                loginLayout?.gone()
            } else {
                chooseAddressLayout?.visible()
                noAddressLayout?.gone()
                loginLayout?.gone()
            }
        }

        renderButton()
    }

    private fun renderButton() {
        buttonLogin?.setOnClickListener {
            startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }

        buttonAddAddress?.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(EXTRA_IS_FULL_FLOW, true)
                putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
            }, REQUEST_CODE_ADD_ADDRESS)
        }

        buttonSnippetLocation?.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS), REQUEST_CODE_GET_DISTRICT_RECOM)
        }
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    private fun showError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"
        const val REQUEST_CODE_ADD_ADDRESS = 199
        const val REQUEST_CODE_GET_DISTRICT_RECOM = 299
    }

    override fun onItemClicked(address: ChosenAddressList) {
        //can't be set due gql
//        viewModel.setStateChosenAddress()

        val data = ChooseAddressUtils.setLocalizingAddressData(address.addressId, address.cityId, address.districtId, address.latitude, address.longitude, address.addressname)
        chooseAddressPref?.setLocalCache(data)
        this.dismiss()
        listener.onAddressDataChanged()
    }

    interface ChooseAddressBottomSheetListener {
        /**
         * this listen if we get server down on widget/bottomshet.
         * Host mandatory to GONE LocalizingAddressWidget
         */
        fun onLocalizingAddressServerDown();

        /**
         * Only use by bottomsheet, to notify every changes in address data
         */
        fun onAddressDataChanged()
    }

}