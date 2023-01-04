package com.tokopedia.play.ui.view.kebab

import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.databinding.ViewKebabIconBinding

/**
 * Created by kenny.hadisaputra on 19/07/22
 */
class KebabIconUiView(
    private val binding: ViewKebabIconBinding,
    listener: Listener,
) {

    private val impressHolder = ImpressHolder()

    init {
        binding.root.setOnClickListener {
            listener.onClicked(this)
        }
        binding.root.addOnImpressionListener(impressHolder){
            listener.onImpressed(this)
        }
    }

    fun show(shouldShow: Boolean) {
        binding.root.showWithCondition(shouldShow)
    }

    interface Listener {
        fun onClicked(view: KebabIconUiView)
        fun onImpressed(view: KebabIconUiView)
    }
}
