package com.tokopedia.chatbot.chatbot2.csat.view.model

data class CsatUiModel(
    var title: String = "",
    var points: List<PointUiModel> = emptyList(),
)

data class PointUiModel(
    var score: Int = 0,
    var caption: String = "",
    var reasonTitle: String = "",
    var reasons: List<String> = emptyList(),
    var otherReasonTitle: String = "",
    var otherReason: String = "",
    var selectedReasons: List<String> = emptyList(),
    var isSelected: Boolean = false
)

val dummyData = CsatUiModel(
    title = "Gimana pengalamanmu dengan Toko Care?",
    points = listOf(
        PointUiModel(
            score = 1,
            caption = "Tidak Memuaskan",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointUiModel(
            score = 2,
            caption = "Tidak Memuaskan",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointUiModel(
            score = 3,
            caption = "Tidak Memuaskan",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?",
            isSelected = true
        ),
        PointUiModel(
            score = 4,
            caption = "Tidak Memuaskan",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointUiModel(
            score = 5,
            caption = "Tidak Memuaskan",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        )
    )
)
