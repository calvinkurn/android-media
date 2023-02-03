package com.tokopedia.tokopedianow.common.view

import com.tokopedia.imageassets.ImageUrl

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowEmptyStateNoAddressBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.user.session.UserSession

class NoAddressEmptyStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val IMG_NO_ADDRESS = ImageUrl.IMG_NO_ADDRESS
    }

    private var binding: LayoutTokopedianowEmptyStateNoAddressBinding? = null

    init {
        binding = LayoutTokopedianowEmptyStateNoAddressBinding.inflate(LayoutInflater.from(context),this, true)
        initRemoteView()
    }

    var actionListener: ActionListener? = null
        set(value) {
            field = value
            field?.let { listener ->
                val userSession = UserSession(context)
                binding?.tokonowEmptyStatePrimaryBtn?.setOnClickListener {
                    listener.onPrimaryBtnClicked()

                    if (listener.onGetNoAddressEmptyStateEventCategoryTracker().isNotEmpty()) {
                        TokoNowCommonAnalytics.onClickChangeAddressOnOoc(
                            userId = userSession.userId,
                            category = listener.onGetNoAddressEmptyStateEventCategoryTracker()
                        )
                    }
                }
                binding?.tokonowEmptyStateSecondaryBtn?.setOnClickListener {
                    listener.onSecondaryBtnClicked()

                    if (listener.onGetNoAddressEmptyStateEventCategoryTracker().isNotEmpty()) {
                        TokoNowCommonAnalytics.onClickShopOnTokopediaOoc(
                            userId = userSession.userId,
                            category = listener.onGetNoAddressEmptyStateEventCategoryTracker()
                        )
                    }
                }
            }
        }

    fun setTitle(title: String) {
        binding?.tokonowEmptyStateTitle?.text = title
    }

    fun setDescription(description: String) {
        binding?.tokonowEmptyStateDesc?.text = description
    }

    fun setPrimaryBtnText(text: String) {
        binding?.tokonowEmptyStatePrimaryBtn?.text = text
    }

    fun setSecondaryBtnText(text: String) {
        binding?.tokonowEmptyStateSecondaryBtn?.text = text
    }

    private fun initRemoteView() {
        binding?.tokonowEmptyStateIcon?.setImageUrl(IMG_NO_ADDRESS)
    }

    interface ActionListener {
        fun onPrimaryBtnClicked()
        fun onSecondaryBtnClicked()
        fun onGetNoAddressEmptyStateEventCategoryTracker(): String
    }
}