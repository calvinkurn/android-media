package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactory

/**
 * Created by Frenzel on 21/04/22.
 */

@MePage(MePage.Widget.WISHLIST)
interface WishlistNavVisitable : Visitable<WishlistTypeFactory>{

}
