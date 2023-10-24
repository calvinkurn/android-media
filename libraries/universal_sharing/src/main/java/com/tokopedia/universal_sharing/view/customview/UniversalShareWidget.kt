package com.tokopedia.universal_sharing.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyResourceIdRef
import com.tokopedia.universal_sharing.R as universal_sharingR

class UniversalShareWidget(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private var channelShareIconId: Int = -1
    private var iconUnifyId: Int = IconUnify.WARNING

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            universal_sharingR.styleable.UniversalShareWidget,
            0, 0).apply {

            try {
                channelShareIconId = getInteger(universal_sharingR.styleable.UniversalShareWidget_channel_share, -1)
                if (channelShareIconId != -1 ) {
                    iconUnifyId = getIconUnifyId()
                    if (iconUnifyId != IconUnify.WARNING) {
                        val icon = getIconUnifyResourceIdRef(channelShareIconId)
                        setOnClickChannel()
                    }
                } else {
                    /* no-op */
                }
            } finally {
                recycle()
            }
        }
    }

    private fun setOnClickChannel() {
        when (channelShareIconId) {
            CHANNEL_WHATSAPP -> {

            }

            else -> {
                /* no-op */
            }
        }
    }

    private fun getIconUnifyId(): Int {
        when (channelShareIconId) {
            CHANNEL_WHATSAPP -> IconUnify.WHATSAPP
            CHANNEL_TELEGRAM -> IconUnify.TELEGRAM
            CHANNEL_SMS -> IconUnify.MESSAGE
            else -> IconUnify.WARNING
        }
    }

    companion object {
        private const val CHANNEL_WHATSAPP = 0
        private const val CHANNEL_TELEGRAM = 1
        private const val CHANNEL_SMS = 2
    }
}
