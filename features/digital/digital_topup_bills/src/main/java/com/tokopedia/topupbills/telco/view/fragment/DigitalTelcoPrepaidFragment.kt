package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.data.TelcoCustomComponentData
import com.tokopedia.topupbills.telco.data.TelcoCustomData
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelcoItem
import com.tokopedia.topupbills.telco.view.model.DigitalPromo
import com.tokopedia.topupbills.telco.view.model.DigitalRecentNumber
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoCustomViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : BaseDaggerFragment() {

    private lateinit var telcoClientNumberWidget: DigitalTelcoClientNumberWidget
    private lateinit var recentNumbersView: DigitalRecentTransactionWidget
    private lateinit var promoListView: DigitalPromoListWidget
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var telcoCustomData: TelcoCustomComponentData
    private val recentNumbers = mutableListOf<DigitalRecentNumber>()
    private val promoList = mutableListOf<DigitalPromo>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private lateinit var customViewModel: DigitalTelcoCustomViewModel
    private lateinit var sharedModel: SharedProductTelcoViewModel

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            customViewModel = viewModelProvider.get(DigitalTelcoCustomViewModel::class.java)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoPrepaidFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productItem.observe(this, Observer {
            it?.run {
                buyWidget.setTotalPrice(it.product.attributes.price)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        viewPager = view.findViewById(R.id.product_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        telcoCustomData = TelcoCustomComponentData(TelcoCustomData(kotlin.collections.mutableListOf()))

        renderInputNumber()
        renderRecentTransactions()
        renderPromoList()
        getInputFilterDataCollections()
    }

    fun getInputFilterDataCollections() {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("componentID", TelcoComponentType.CLIENT_NUMBER)
        customViewModel.getCustomData(GraphqlHelper.loadRawString(resources, R.raw.query_custom_digital_telco), mapParam,
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.telcoCustomData = telcoData
        renderProductListFromInputfilter()
    }

    fun renderProductListFromInputfilter() {
        val prefixClientNumber = telcoClientNumberWidget.getInputNumber().substring(0, 4)
        try {
            val dataFound = this.telcoCustomData.rechargeCustomData.customDataCollections.filter {
                it.value.equals(prefixClientNumber)
            }.single()

            renderViewPager(dataFound.operator.id)
            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        } catch (exception: Exception) {
            Toast.makeText(activity, "error exception", Toast.LENGTH_SHORT).show()
        }
    }

    fun onErrorCustomData(throwable: Throwable) {
        Toast.makeText(activity, "input filter " + throwable.message, Toast.LENGTH_SHORT).show()
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalBaseClientNumberWidget.ActionListener {
            override fun navigateToContact() {
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
                                }, "")
                    }
                } else {
                    openContactPicker()
                }
            }

            override fun renderProductList() {
                telcoCustomData.rechargeCustomData.customDataCollections.isEmpty()?.let {
                    if (it) {
                        getInputFilterDataCollections()
                    } else {
                        renderProductListFromInputfilter()
                    }
                }
            }

            override fun clearAutoComplete() {
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
            }
        })
    }

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            NetworkErrorHelper.showSnackbar(activity,
                    getString(R.string.error_message_contact_not_found))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.run {
            permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        }
    }

    fun renderViewPager(operatorId: String) {
        val listProductTab = mutableListOf<DigitalProductTelcoItem>()
        listProductTab.add(DigitalProductTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PULSA, operatorId, TelcoProductType.PRODUCT_GRID), "Pulsa"))
        listProductTab.add(DigitalProductTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PAKET_DATA, operatorId, TelcoProductType.PRODUCT_LIST), "Paket Data"))
        listProductTab.add(DigitalProductTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_ROAMING, operatorId, TelcoProductType.PRODUCT_LIST), "Roaming"))
        val pagerAdapter = DigitalTelcoProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        val contactURI = data.data
                        val contact = contactURI.covertContactUriToContactData(it.contentResolver)
                        telcoClientNumberWidget.setInputNumber(contact.contactNumber)
                    }
                }
            }
        }
    }

    //TODO IMPLEMENT API
    fun renderRecentTransactions() {
        recentNumbersView.setListener(object : DigitalRecentTransactionWidget.ActionListener {
            override fun onClickRecentNumber(digitalRecentNumber: DigitalRecentNumber) {
                Toast.makeText(activity, digitalRecentNumber.clientNumber, Toast.LENGTH_LONG).show()
            }
        })
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 1", "081723823112", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 2", "082313134234", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 3", "081342424234", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 4", "087823173712", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 5", "082343432423", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 6", "081231313233", "", "", 316, "Simpati"))
        recentNumbersView.setRecentNumbers(recentNumbers)
    }

    //TODO IMPLEMENT API
    fun renderPromoList() {
        promoListView.setListener(object : DigitalPromoListWidget.ActionListener {
            override fun onCopiedPromoCode(voucherCode: String) {
                Toast.makeText(activity, "Kode voucher telah di copy ke clipboard", Toast.LENGTH_LONG).show()
            }
        })
        promoList.add(DigitalPromo("1", "Cashback hingga Rp400.000 (khusus pengguna baru). S&K lengkap klik di sini.", "TOPEDLALA", false, ""))
        promoList.add(DigitalPromo("2", "Cashback hingga Rp400.000 (khusus pengguna baru). S&K lengkap klik di sini.", "TOPEDLELE", false, ""))
        promoList.add(DigitalPromo("3", "Cashback hingga Rp400.000 (khusus pengguna baru). S&K lengkap klik di sini.", "TOPEDLILI", false, ""))
        promoList.add(DigitalPromo("4", "Cashback hingga Rp400.000 (khusus pengguna baru). S&K lengkap klik di sini.", "TOPEDLULU", false, ""))
        promoList.add(DigitalPromo("5", "Cashback hingga Rp400.000 (khusus pengguna baru). S&K lengkap klik di sini.", "TOPEDLOLO", false, ""))
        promoListView.setPromoList(promoList)
    }

    override fun onDestroy() {
        customViewModel.clear()
        super.onDestroy()
    }

    companion object {

        val REQUEST_CODE_CONTACT_PICKER = 78;

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}