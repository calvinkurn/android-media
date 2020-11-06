package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.Info
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

class ShopInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(info: Info?): ShopInfoUiModel {
        if (null == info) return ShopInfoUiModel()
        return ShopInfoUiModel(
                shopName = info.shopName,
                shopAvatar = info.shopAvatar
        )
    }
}