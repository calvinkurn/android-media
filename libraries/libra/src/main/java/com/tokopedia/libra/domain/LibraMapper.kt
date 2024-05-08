package com.tokopedia.libra.domain

import com.tokopedia.libra.data.entity.LibraResponse
import com.tokopedia.libra.domain.model.ItemLibraUiModel
import com.tokopedia.libra.domain.model.LibraUiModel

object LibraMapper {

    fun map(response: LibraResponse): LibraUiModel {
        return LibraUiModel(
            experiments = response.experiments.map {
                ItemLibraUiModel(
                    experiment = it.experiment,
                    variant = it.variant
                )
            }
        )
    }
}
