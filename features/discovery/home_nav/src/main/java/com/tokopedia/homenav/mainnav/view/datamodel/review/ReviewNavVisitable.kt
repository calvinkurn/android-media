package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactory

/**
 * Created by Frenzel on 18/04/22.
 */

@MePage(MePage.Widget.REVIEW)
interface ReviewNavVisitable : Visitable<ReviewTypeFactory>{

}
