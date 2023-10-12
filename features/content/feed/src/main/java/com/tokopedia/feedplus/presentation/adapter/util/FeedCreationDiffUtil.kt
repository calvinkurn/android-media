package com.tokopedia.feedplus.presentation.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem

internal class FeedCreationDiffUtil : DiffUtil.ItemCallback<ContentCreationTypeItem>() {

        override fun areItemsTheSame(oldItem: ContentCreationTypeItem, newItem: ContentCreationTypeItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContentCreationTypeItem, newItem: ContentCreationTypeItem): Boolean {
            return oldItem == newItem
        }
    }

