package com.tokopedia.salam.umrah.homepage.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

abstract class  UmrahHomepageModel(var isLoaded: Boolean = false,
                                   var isSuccess: Boolean = true,
                                   var isLoadFromCloud: Boolean = false) : Visitable<UmrahHomepageFactory>