package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.WidgetRechargeOmniBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeOmniWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetRechargeOmniBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderOmniWidget(applink: String) {
        binding.run {
            imgOmniIcon.loadImage(IMG_URL)
            root.setOnClickListener {
                listener.onClickOmniWidget(applink)
            }
        }
    }

    interface RechargeOmniWidgetListener {
        fun onClickOmniWidget(applink: String)
    }

    companion object {
        private const val IMG_URL = "https://images.tokopedia.net/img/recharge/telkomsel_omni_icon.png"
    }
}
