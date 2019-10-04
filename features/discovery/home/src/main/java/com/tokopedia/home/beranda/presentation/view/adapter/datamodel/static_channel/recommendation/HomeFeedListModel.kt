package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation


import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory

class HomeFeedListModel(var homeFeedViewModels: List<Visitable<HomeFeedTypeFactory>>?, var isHasNextPage: Boolean)
