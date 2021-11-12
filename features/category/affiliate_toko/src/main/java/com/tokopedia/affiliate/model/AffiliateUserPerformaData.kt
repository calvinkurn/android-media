package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import java.util.ArrayList

class AffiliateUserPerformaData(
      var data : ArrayList<Visitable<AffiliateAdapterTypeFactory>>? = null
)