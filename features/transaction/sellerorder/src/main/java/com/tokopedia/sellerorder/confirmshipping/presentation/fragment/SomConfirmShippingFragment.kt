package com.tokopedia.sellerorder.confirmshipping.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_SHIPMENT_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_SHIPMENT_PRODUCT_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_SCAN_BARCODE
import com.tokopedia.sellerorder.confirmshipping.di.SomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomScanResiActivity
import com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel.SomConfirmShippingViewModel
import kotlinx.android.synthetic.main.fragment_som_confirm_shipping.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var currOrderId = ""
    private var currShipmentName = ""
    private var currShipmentProductName = ""
    private val FLAG_SCAN_BARCODE = 3003

    private val somConfirmShippingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomConfirmShippingViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomConfirmShippingFragment {
            return SomConfirmShippingFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                    putString(PARAM_CURR_SHIPMENT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_NAME))
                    putString(PARAM_CURR_SHIPMENT_PRODUCT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_PRODUCT_NAME))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currOrderId = arguments?.getString(PARAM_ORDER_ID).toString()
            currShipmentName = arguments?.getString(PARAM_CURR_SHIPMENT_NAME).toString()
            currShipmentProductName = arguments?.getString(PARAM_CURR_SHIPMENT_PRODUCT_NAME).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_confirm_shipping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
    }

    private fun setupLayout() {
        tf_no_resi?.apply {
            setLabelStatic(true)
            textFiedlLabelText.text = getString(R.string.nomor_resi)
            setMessage(getString(R.string.tf_no_resi_message))
            setPlaceholder(getString(R.string.tf_no_resi_placeholder))
            setFirstIcon(R.drawable.ic_scanbarcode)
        }
        label_choosen_courier?.text = currShipmentName
        label_choosen_courier_service?.text = currShipmentProductName
    }

    private fun setupListeners() {
        // set onclick scan resi
        tf_no_resi?.getFirstIcon()?.setOnClickListener {
            Intent(activity, SomScanResiActivity::class.java).apply {
                startActivityForResult(this, FLAG_SCAN_BARCODE)
            }
        }

        // set onchange switch
        switch_change_courier?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) cl_change_courier?.visibility = View.VISIBLE
            else cl_change_courier?.visibility = View.GONE
        }

        // set onclick kurir & paket pengiriman (bikin viewmodel nya dulu)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomConfirmShippingComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_SCAN_BARCODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_SCAN_BARCODE)) {
                    val newResi = data.getStringExtra(RESULT_SCAN_BARCODE)
                    tf_no_resi?.textFieldInput?.setText(newResi)
                    /*activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_SCAN_BARCODE, newResi)
                    })
                    activity?.finish()*/
                }
            }
        }
    }
}