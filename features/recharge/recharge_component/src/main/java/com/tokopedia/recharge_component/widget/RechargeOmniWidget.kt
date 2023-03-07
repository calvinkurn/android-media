package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.WidgetRechargeOmniBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeOmniWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetRechargeOmniBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderOmniWidget(listener: RechargeOmniWidgetListener, applink: String) {
        binding.run {
            shimmeringOmni.root.hide()
            if (applink.isNotEmpty()) {
                imgOmniIcon.show()
                iconOmniChevron.show()
                tgOmniTitle.show()
                tgOmniDesc.show()
                imgOmniIcon.loadImage(IMG_URL)
                root.setOnClickListener {
                    listener.onClickOmniWidget(applink)
                }
            }
        }
    }

    fun renderShimmering() {
        with(binding) {
            imgOmniIcon.hide()
            iconOmniChevron.hide()
            tgOmniTitle.hide()
            tgOmniDesc.hide()
            shimmeringOmni.root.show()
        }
    }

    interface RechargeOmniWidgetListener {
        fun onClickOmniWidget(applink: String)
    }

    companion object {
        private const val IMG_URL = "https://images.tokopedia.net/img/recharge/telkomsel_omni_icon.png"
    }
}
