package com.tokopedia.verification.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.verification.R
import com.tokopedia.verification.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_notif_result

    var mainImage: ImageUnify? = null
    var title: Typography? = null
    var subtitle: Typography? = null
    var btnMain: UnifyButton? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                mainImage = findViewById(R.id.main_image)
                title = findViewById(R.id.text_title)
                subtitle = findViewById(R.id.text_subtitle)
                btnMain = findViewById(R.id.button_main)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}
