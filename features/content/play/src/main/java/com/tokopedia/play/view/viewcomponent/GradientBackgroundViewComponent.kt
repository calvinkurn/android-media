package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.databinding.ViewGradientBackgroundBinding
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 24/02/23
 */
class GradientBackgroundViewComponent(container: ViewGroup) : ViewComponent(
    container, R.id.view_gradient_background,
) {

    private val binding = ViewGradientBackgroundBinding.bind(rootView)

    fun showTop() {
        binding.vGradientBackgroundTop.show()
        binding.vGradientBackgroundBottom.hide()

        show()
    }

    fun showBottom() {
        binding.vGradientBackgroundTop.hide()
        binding.vGradientBackgroundBottom.show()

        show()
    }
}
