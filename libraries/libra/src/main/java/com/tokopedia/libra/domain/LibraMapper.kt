package com.tokopedia.libra.domain

import com.tokopedia.libra.LibraResult
import com.tokopedia.libra.data.entity.LibraResponse
import com.tokopedia.libra.domain.model.LibraUiModel

internal object LibraMapper {

    fun map(response: LibraResponse): LibraUiModel {
        return LibraUiModel(
            experiments = response.experiments.map {
                LibraResult(
                    experiment = it.experiment,
                    variant = it.variant
                )
            }
        )
    }
}
