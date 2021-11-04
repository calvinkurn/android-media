package com.tokopedia.digital.home.old.model

import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchViewHolder

enum class DigitalHomepageSearchEnumLayoutType(val layoutTemplate: String, val layoutType: Int) {
    DEFAULT("", DigitalHomePageSearchViewHolder.LAYOUT),
    SINGLE_LINE("list_single_line", DigitalHomePageSearchViewHolder.LAYOUT),
    DOUBLE_LINE("list_double_line", DigitalHomePageSearchViewHolder.LAYOUT),
    HEADER("header", DigitalHomePageSearchViewHolder.LAYOUT)
}