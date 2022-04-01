package com.tokopedia.affiliate.model.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import java.util.*

class AffiliateUserPerformaData(
      var data : ArrayList<Visitable<AffiliateAdapterTypeFactory>>? = null,
      var itemCount : Int? = 0,
      var showProductCount:Boolean = true
)