package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.play.R
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.unifycomponents.R as unifyCompR
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener,
    private val fragmentManager: FragmentManager,
) : ViewComponent(container, R.id.view_play_widget_address) {

    private lateinit var chooseAddressBottomSheet: ChooseAddressBottomSheet
    private val btnChoose: UnifyButton = findViewById(R.id.btn_change_address)
    private val tvInfo: TextView = findViewById(R.id.tv_address_desc)

    private val insideListener = object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener{
        override fun onLocalizingAddressServerDown() {}

        override fun onAddressDataChanged() {
            listener.onAddressUpdated(this@ChooseAddressViewComponent)
        }

        override fun getLocalizingAddressHostSourceBottomSheet(): String = ADDRESS_WIDGET_SOURCE

        override fun onLocalizingAddressLoginSuccessBottomSheet() {
            listener.onAddressUpdated(this@ChooseAddressViewComponent)
        }

        override fun onDismissChooseAddressBottomSheet() {
            hideBottomSheet()
        }
    }

    private val ctx: Context get() = rootView.context
    private val trackingField = ImpressHolder()

    init {
        btnChoose.setOnClickListener {
            openBottomSheet()
        }

        tvInfo.text = MethodChecker.fromHtml(getString(R.string.play_address_widget_info))
        tvInfo.setOnClickListener {
            listener.onInfoClicked(this@ChooseAddressViewComponent)
        }

        setupButtonView()

        rootView.addImpressionListener(trackingField) {
            listener.onImpressedAddressWidget(this@ChooseAddressViewComponent)
        }

        btnChoose.addImpressionListener(trackingField) {
            listener.onImpressedBtnChoose(this@ChooseAddressViewComponent)
        }
    }

    private fun openBottomSheet() {
        val addressSheet = getBottomSheet()
        if (!addressSheet.isVisible) {
            listener.onBtnChooseClicked(this@ChooseAddressViewComponent)
            addressSheet.showNow(fragmentManager, PLAY_CHOOSE_ADDRESS_TAG)
        }
    }

    fun hideBottomSheet() {
        val addressSheet = getBottomSheet()
        if (addressSheet.isVisible) addressSheet.dismiss()
    }

    private fun getBottomSheet() : ChooseAddressBottomSheet {
        chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(insideListener)
        return chooseAddressBottomSheet
    }

    private fun setupButtonView() {
        val bg = GradientDrawable().apply {
            cornerRadius = ctx.resources.getDimensionPixelSize(unifyR.dimen.layout_lvl1).toFloat()
            setStroke(ctx.resources.getDimensionPixelSize(unifyCompR.dimen.button_stroke_width), MethodChecker.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        }
        btnChoose.background = bg
    }

    companion object {
        private const val PLAY_CHOOSE_ADDRESS_TAG = "PLAY_ADDRESS"
        private const val ADDRESS_WIDGET_SOURCE = "play"
    }

    interface Listener {
        fun onAddressUpdated(view: ChooseAddressViewComponent)
        fun onInfoClicked(view: ChooseAddressViewComponent)
        fun onImpressedAddressWidget(view: ChooseAddressViewComponent)
        fun onImpressedBtnChoose(view: ChooseAddressViewComponent)
        fun onBtnChooseClicked(view: ChooseAddressViewComponent)
    }
}