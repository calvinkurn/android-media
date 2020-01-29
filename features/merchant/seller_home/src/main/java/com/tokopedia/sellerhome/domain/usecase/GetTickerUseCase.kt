package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.sellerhome.data.GetTickerRepository
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

class GetTickerUseCase @Inject constructor(
        private val tickerRepo: GetTickerRepository
) : UseCase<List<TickerUiModel>>() {

    override suspend fun executeOnBackground(): List<TickerUiModel> {
        return /*tickerRepo.getTicker()*/ getDummyTickerList()
    }

    private fun getDummyTickerList(): List<TickerUiModel> {
        return listOf(
                TickerUiModel(
                        "https://tokopedia.com/",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "Ini adalah contoh pesan ticker yang pertama",
                        "",
                        "",
                        "",
                        "", "",
                        "#dddddd"
                ),
                TickerUiModel(
                        "https://tokopedia.com/",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "Ini adalah contoh pesan ticker yang ke dua",
                        "",
                        "",
                        "",
                        "", "",
                        "#ffffff"
                ),
                TickerUiModel(
                        "https://tokopedia.com/",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "Ini adalah contoh pesan ticker yang ke tiga",
                        "",
                        "",
                        "",
                        "", "",
                        "#000000"
                )
        )
    }
}