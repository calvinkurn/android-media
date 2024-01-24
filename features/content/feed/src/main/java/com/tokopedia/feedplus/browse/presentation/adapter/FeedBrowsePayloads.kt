package com.tokopedia.feedplus.browse.presentation.adapter

/**
 * Created by kenny.hadisaputra on 19/09/23
 */
data class FeedBrowsePayloads(
    val payloads: Set<Int>,
) {

    class Builder {

        private val payloads = mutableSetOf<Int>()

        fun addChannelItemsChanged() = addPayload(PAYLOAD_CHANNEL_ITEMS_CHANGED)

        fun addChannelChipsChanged() = addPayload(PAYLOAD_CHANNEL_CHIPS_CHANGED)

        fun addChannelRefresh() = addPayload(PAYLOAD_CHANNEL_REFRESH)

        fun addSelectedChipChanged() = addPayload(PAYLOAD_SELECTED_CHIP_CHANGED)

        fun addChannelItemTotalViewChanged() = addPayload(PAYLOAD_CHANNEL_ITEM_TOTAL_VIEW_CHANGED)

        fun build(): FeedBrowsePayloads? {
            return if (payloads.isEmpty()) {
                null
            } else {
                FeedBrowsePayloads(payloads)
            }
        }

        private fun addPayload(payload: Int) = builder {
            payloads.add(payload)
        }

        private fun builder(onBuild: () -> Unit): Builder {
            onBuild()
            return this
        }
    }

    fun isChannelItemsChanged(): Boolean {
        return payloads.contains(PAYLOAD_CHANNEL_ITEMS_CHANGED)
    }

    fun isChannelChipsChanged(): Boolean {
        return payloads.contains(PAYLOAD_CHANNEL_CHIPS_CHANGED)
    }

    fun isSelectedChipChanged(): Boolean {
        return payloads.contains(PAYLOAD_SELECTED_CHIP_CHANGED)
    }

    fun isChannelRefresh(): Boolean {
        return payloads.contains(PAYLOAD_CHANNEL_REFRESH)
    }

    fun isChannelItemTotalViewChanged(): Boolean {
        return payloads.contains(PAYLOAD_CHANNEL_ITEM_TOTAL_VIEW_CHANGED)
    }

    companion object {
        private const val PAYLOAD_CHANNEL_CHIPS_CHANGED = 1
        private const val PAYLOAD_CHANNEL_ITEMS_CHANGED = 2
        private const val PAYLOAD_CHANNEL_REFRESH = 3
        private const val PAYLOAD_SELECTED_CHIP_CHANGED = 4

        private const val PAYLOAD_CHANNEL_ITEM_TOTAL_VIEW_CHANGED = 10
    }
}

fun List<FeedBrowsePayloads>.combine(): FeedBrowsePayloads {
    return FeedBrowsePayloads(flatMap { it.payloads }.toSet())
}
