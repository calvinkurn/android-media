package com.tokopedia.selleronboarding.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SobAdapter : BaseListAdapter<Visitable<SobAdapterFactory>, SobAdapterFactoryImpl>(SobAdapterFactoryImpl())