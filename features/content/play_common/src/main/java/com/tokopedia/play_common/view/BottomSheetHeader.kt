package com.tokopedia.play_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.databinding.BottomSheetHeaderNewBinding
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created by kenny.hadisaputra on 13/03/23
 */
class BottomSheetHeader : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = BottomSheetHeaderNewBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var mListener: Listener? = null

    init {
        binding.ivSheetClose.setOnClickListener {
            mListener?.onCloseClicked(this)
        }

        binding.iconNotifRight.setOnClickListener {
            mListener?.onIconClicked(this)
        }
        binding.iconNotifRight.addImpressionListener(ImpressHolder()){
            if (binding.iconNotifRight.isVisible) mListener?.impressIcon(this)
        }
    }

    fun setTitle(title: String) {
        binding.tvSheetTitle.text = title
    }

    fun showTitle(shouldShow: Boolean) {
        binding.tvSheetTitle.showWithCondition(shouldShow)
    }

    fun setIconNotification(
        iconUnifyId: Int?,
        lightEnable: Int? = null,
        lightDisable: Int? = null,
        darkEnable: Int? = null,
        darkDisable: Int? = null
    ) {
        binding.iconNotifRight.showWithCondition(iconUnifyId != null)
        binding.iconNotifRight.setImageWithUnifyIcon(
            newIconId = iconUnifyId,
            newLightEnable = lightEnable,
            newLightDisable = lightDisable,
            newDarkEnable = darkEnable,
            newDarkDisable = darkDisable,
        )
    }

    fun setIconNotificationText(text: String) {
        binding.iconNotifRight.notificationRef.showWithCondition(text.isNotBlank())

        binding.iconNotifRight.notificationRef.setNotification(
            text,
            NotificationUnify.COUNTER_TYPE,
            NotificationUnify.COLOR_PRIMARY,
        )
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onCloseClicked(view: BottomSheetHeader)
        fun onIconClicked(view: BottomSheetHeader)

        fun impressIcon(view: BottomSheetHeader)
    }
}
