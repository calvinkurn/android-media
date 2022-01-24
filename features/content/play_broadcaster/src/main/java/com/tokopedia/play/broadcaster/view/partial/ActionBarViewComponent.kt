package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 12/06/20.
 */
class ActionBarViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.cl_actionbar){

    private val ivClose = findViewById<IconUnify>(R.id.ic_bro_preparation_close)
    private val tvTitle = findViewById<Typography>(R.id.tv_bro_preparation_shop_name)
    private val ivShopIcon = findViewById<ImageUnify>(R.id.iv_bro_preparation_shop_icon)

    init {
        ivClose.setOnClickListener { listener.onCloseIconClicked() }
        ivClose.show()
    }

    fun setTitle(label: String) {
        tvTitle.text = label
    }

    fun setShopIcon(iconUrl: String) {
        ivShopIcon.setImageUrl(iconUrl)
    }

    interface Listener {
        fun onCameraIconClicked()
        fun onCloseIconClicked()
    }
}