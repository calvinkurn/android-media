package com.tokopedia.notifcenter.view.buyer.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.InboxCounter
import com.tokopedia.notifcenter.view.customview.NotifCenterBadgeCounterUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

abstract class NotifCenterBaseAccountSwitcherMenuItem : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    @RoleType
    abstract val role: Int

    protected var name: Typography? = null
    private var photoProfile: ImageUnify? = null
    protected var smallIcon: ImageView? = null
    private var unreadCounter: Typography? = null
    private var checkMark: ImageView? = null

    init {
        initView()
    }

    fun setName(name: String?) {
        name?.let {
            this.name?.text = MethodChecker.fromHtml(name)
        }
    }

    fun loadProfilePicture(imageUrl: String?) {
        imageUrl ?: return
        photoProfile?.scaleType = ImageView.ScaleType.CENTER_CROP
        photoProfile?.setImageUrl(imageUrl)
    }

    fun showCheckMark() {
        checkMark?.show()
    }

    fun hideCheckMark() {
        checkMark?.hide()
    }

    protected open fun onCreateView(view: View) {}

    private fun initView() {
        View.inflate(context, R.layout.partial_inbox_account_menu_item, this)?.apply {
            initViewBinding(this)
            onCreateView(this)
        }
    }

    private fun initViewBinding(view: View) {
        name = view.findViewById(R.id.tv_name)
        photoProfile = view.findViewById(R.id.iv_photo_profile)
        smallIcon = view.findViewById(R.id.iv_small_icon)
        unreadCounter = view.findViewById(R.id.unread_counter)
        checkMark = view.findViewById(R.id.iv_checkmark)
    }

    fun bindBadgeCounter(inboxCounter: InboxCounter) {
        val roleInboxCounter = inboxCounter.getByRole(role)
        NotifCenterBadgeCounterUtil.setBadgeCounter(
            unreadCounter,
            roleInboxCounter?.notifcenterInt
        )
    }
}
