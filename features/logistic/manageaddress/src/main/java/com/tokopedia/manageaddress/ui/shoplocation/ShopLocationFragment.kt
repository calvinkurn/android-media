package com.tokopedia.manageaddress.ui.shoplocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.shoplocation.ShopLocationComponent
import com.tokopedia.manageaddress.util.ManageAddressConstant.BOTTOMSHEET_TITLE_ATUR_LOKASI
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_action_shop_address.view.*

class ShopLocationFragment : BaseDaggerFragment(), ShopLocationItemAdapter.ShopLocationItemAdapterListener {

    private var addressList: RecyclerView? = null
    private var bottomSheetAddressType: BottomSheetUnify? = null

    private var globalErrorLayout: GlobalError? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipment_address_list, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        addressList = view?.findViewById(R.id.address_list)
        globalErrorLayout = view?.findViewById(R.id.global_error)
    }

    /* activate/deactivate shop location */
    private fun openBottomSheetAddressType() {
        val addressActiveState = true
        bottomSheetAddressType = BottomSheetUnify()
        val viewBottomSheetAddressType = View.inflate(context, R.layout.bottomsheet_deactivate_location, null).apply {
            if (addressActiveState) {
                btn_set_location_status.text = "Nonaktifkan Lokasi"
                btn_set_location_status.setOnClickListener {
                    Toast.makeText(context, "Deactivate Location", Toast.LENGTH_SHORT).show()
                    bottomSheetAddressType?.dismiss()
                }
            } else {
                btn_set_location_status.text = "Aktifkan Lokasi"
                btn_set_location_status.setOnClickListener {
                    Toast.makeText(context, "Activate Location", Toast.LENGTH_SHORT).show()
                    bottomSheetAddressType?.dismiss()
                }
            }
        }

        bottomSheetAddressType?.apply {
            setTitle(BOTTOMSHEET_TITLE_ATUR_LOKASI)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressType)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressType?.show(it, "show")
        }
    }

    override fun onShopLocationStateStatusClicked() {
        openBottomSheetAddressType()
    }

}