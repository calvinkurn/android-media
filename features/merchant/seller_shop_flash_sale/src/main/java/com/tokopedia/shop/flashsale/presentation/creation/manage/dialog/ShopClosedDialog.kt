package com.tokopedia.shop.flashsale.presentation.creation.manage.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.customcomponent.ModalBottomSheet
import com.tokopedia.unifycomponents.UnifyButton

class ShopClosedDialog : ModalBottomSheet() {

    companion object {
        const val TAG = "ShopClosedDialog"
    }

    var primaryCTAAction: () -> Unit = {}
    var secondaryCTAAction: () -> Unit = {}

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    private var primaryCTA: UnifyButton? = null
    private var secondaryCTA: UnifyButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        MODAL_WIDTH_RATIO = 0.9
        val contentView: View? = View.inflate(context, R.layout.ssfs_dialog_shop_closed, null)
        primaryCTA = contentView?.findViewById(R.id.btn_buka_toko)
        secondaryCTA = contentView?.findViewById(R.id.btn_batal)
        initContent()
        setChild(contentView)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, TAG)
        }
    }

    private fun initContent() {
        showCloseIcon = false
        primaryCTA?.setOnClickListener {
            primaryCTAAction.invoke()
        }
        secondaryCTA?.setOnClickListener {
            secondaryCTAAction.invoke()
        }
    }
}