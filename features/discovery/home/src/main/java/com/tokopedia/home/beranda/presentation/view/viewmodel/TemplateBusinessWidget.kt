package com.tokopedia.home.beranda.presentation.view.viewmodel

enum class TemplateBusinessWidget {
    BigMultiItems {
        override fun type() = BigMultiItems
    },
    BigOneItemBold {
        override fun type() = BigOneItemBold
    },
    MultiItems {
        override fun type() = MultiItems
    },
    OneItem {
        override fun type() = OneItem
    },
    None {
        override fun type() = None
    };

    abstract fun type(): TemplateBusinessWidget
}