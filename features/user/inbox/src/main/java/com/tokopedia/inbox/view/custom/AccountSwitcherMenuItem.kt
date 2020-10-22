package com.tokopedia.inbox.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.inbox.R
import com.tokopedia.unifyprinciples.Typography

abstract class AccountSwitcherMenuItem : ConstraintLayout {

    private var name: Typography? = null
    private var photoProfile: ImageView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        initView()
    }

    fun setName(name: String?) {
        this.name?.text = name
    }

    fun loadProfilePicture(imageUrl: String?) {
        ImageHandler.loadImageCircle2(context, photoProfile, imageUrl)
    }

    private fun initView() {
        View.inflate(context, R.layout.partial_inbox_account_menu_item, this)?.apply {
            initViewBinding(this)
        }
    }

    private fun initViewBinding(view: View) {
        name = view.findViewById(R.id.tv_name)
        photoProfile = view.findViewById(R.id.iv_photo_profile)
    }
}