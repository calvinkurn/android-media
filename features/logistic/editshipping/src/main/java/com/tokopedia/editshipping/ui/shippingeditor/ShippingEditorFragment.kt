package com.tokopedia.editshipping.ui.shippingeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class ShippingEditorFragment: BaseDaggerFragment(), ShippingEditorItemAdapter.ShippingEditorItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShippingEditorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShippingEditorViewModel::class.java)
    }

    private var tickerShipperInfo: Ticker? = null
    private var shipperListOnDemand: RecyclerView? = null
    private var shipperListConventional: RecyclerView? = null
    private var btnSaveShipper: UnifyButton? = null

    private var bottomSheetShipperInfo: BottomSheetUnify? = null
    private var bottomSheetImageInfo: DeferredImageView? = null
    private var bottomSheetInfoCourier: Typography? = null
    private var bottomSheetInfoCourierDetail: Typography? = null
    private var btnShipperBottomSheet: UnifyButton? = null
    private var bottomSheetShipperInfoType: Int? = -1

    private var globalErrorLayout: GlobalError? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShippingEditorComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_editor_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        tickerShipperInfo = view?.findViewById(R.id.ticker_shipper_info)
        shipperListOnDemand = view?.findViewById(R.id.rv_on_demand)
        shipperListConventional = view?.findViewById(R.id.rv_conventional)
        btnSaveShipper = view?.findViewById(R.id.btn_save_shipper)
        globalErrorLayout = view?.findViewById(R.id.global_error)
    }

    override fun onShipperInfoClicker() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun openBottomSheetShipperInfo() {
        bottomSheetShipperInfo = BottomSheetUnify()
        val viewBottomSheetShipperInfo = View.inflate(context, R.layout.bottomsheet_shipper_info, null)
        setupBottomSheetShipperInfoChild(viewBottomSheetShipperInfo)

        bottomSheetShipperInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetShipperInfo)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetShipperInfo?.show(it, "show")
        }
    }

    private fun setupBottomSheetShipperInfoChild(child: View) {
        bottomSheetImageInfo = child.findViewById(R.id.img_info_courier)
        bottomSheetInfoCourier = child.findViewById(R.id.tv_info_courier)
        bottomSheetInfoCourierDetail = child.findViewById(R.id.tv_info_courier_detail)
        btnShipperBottomSheet = child.findViewById(R.id.btn_close)

        if(bottomSheetShipperInfoType == 1) {
            bottomSheetImageInfo?.loadRemoteImageDrawable("AWB")
            bottomSheetInfoCourier?.text = getString(R.string.awb_otomatis_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.awb_otomatis_detail)

        } else {
            bottomSheetImageInfo?.loadRemoteImageDrawable("Non Tunai")
            bottomSheetInfoCourier?.text = getString(R.string.non_tunai_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.non_tunai_detail)
        }
        btnShipperBottomSheet?.setOnClickListener { bottomSheetShipperInfo?.dismiss() }
    }


}