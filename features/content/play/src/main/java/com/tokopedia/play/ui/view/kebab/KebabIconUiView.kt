package com.tokopedia.play.ui.view.kebab

import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.databinding.ViewKebabIconBinding

/**
 * Created by kenny.hadisaputra on 19/07/22
 */
class KebabIconUiView(
    private val binding: ViewKebabIconBinding,
    listener: Listener,
) {

    init {
        binding.root.setOnClickListener {
            listener.onClicked(this)
        }
    }

    fun show(shouldShow: Boolean) {
        binding.root.showWithCondition(shouldShow)
    }

    interface Listener {
        fun onClicked(view: KebabIconUiView)
    }
}