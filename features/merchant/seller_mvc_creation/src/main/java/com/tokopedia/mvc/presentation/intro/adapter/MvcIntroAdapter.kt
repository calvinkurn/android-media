package com.tokopedia.mvc.presentation.intro.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.mvc.presentation.intro.adapter.factory.MvcIntroAdapterFactoryImpl

class MvcIntroAdapter: BaseAdapter<MvcIntroAdapterFactoryImpl>(MvcIntroAdapterFactoryImpl())
