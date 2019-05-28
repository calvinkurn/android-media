package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.ticker.TickerView
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.data.TelcoCustomComponentData
import com.tokopedia.topupbills.telco.data.TelcoCustomData
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalPromo
import com.tokopedia.topupbills.telco.view.model.DigitalRecentNumber
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoCustomViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalPromoListWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalRecentTransactionWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
open abstract class DigitalBaseTelcoFragment : BaseDaggerFragment() {

    protected lateinit var tickerView: TickerView
    protected lateinit var recentNumbersView: DigitalRecentTransactionWidget
    protected lateinit var promoListView: DigitalPromoListWidget
    private lateinit var customViewModel: DigitalTelcoCustomViewModel

    private val recentNumbers = mutableListOf<DigitalRecentNumber>()
    private val promoList = mutableListOf<DigitalPromo>()

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            customViewModel = viewModelProvider.get(DigitalTelcoCustomViewModel::class.java)
        }
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomData(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco), getMapCustomData(),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    protected abstract fun onSuccessCustomData(telcoData: TelcoCustomComponentData)

    protected abstract fun onErrorCustomData(error: Throwable)

    protected abstract fun getMapCustomData(): Map<String, kotlin.Any>

    fun renderTicker() {
        val messages = ArrayList<String>()
        messages.add("Untuk paket data Indosat, telkomsel dan tri akan tersedia kembali pukul 18.00 WIB")
        tickerView.setListMessage(messages)
        tickerView.buildView()
        tickerView.visibility = View.VISIBLE
    }

    fun navigateContact() {
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

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, DigitalTelcoPrepaidFragment.REQUEST_CODE_CONTACT_PICKER)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == DigitalTelcoPrepaidFragment.REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        val contactURI = data.data
                        val contact = contactURI.covertContactUriToContactData(it.contentResolver)
                        setInputNumberFromContact(contact.contactNumber)
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

    abstract fun setInputNumberFromContact(contactNumber: String)

    override fun onDestroy() {
        customViewModel.clear()
        super.onDestroy()
    }
}