package com.tokopedia.topupbills.telco.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.model.DigitalPromo
import com.tokopedia.topupbills.telco.model.DigitalRecentNumber
import com.tokopedia.topupbills.telco.widget.DigitalBaseClientNumberWidget
import com.tokopedia.topupbills.telco.widget.DigitalPromoListWidget
import com.tokopedia.topupbills.telco.widget.DigitalRecentTransactionWidget
import com.tokopedia.topupbills.telco.widget.DigitalTelcoClientNumberWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : BaseDaggerFragment() {

    private lateinit var telcoClientNumberWidget: DigitalTelcoClientNumberWidget
    private lateinit var recentNumbersView: DigitalRecentTransactionWidget
    private lateinit var promoListView: DigitalPromoListWidget
    private val recentNumbers = mutableListOf<DigitalRecentNumber>()
    private val promoList = mutableListOf<DigitalPromo>()

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    override fun getScreenName(): String {
        return DigitalTelcoPrepaidFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderInputNumber()
        renderRecentTransactions()
        renderPromoList()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.run {
            permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        }
    }

    companion object {

        val REQUEST_CODE_CONTACT_PICKER = 78;

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}