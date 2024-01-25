package com.tokopedia.chatbot.chatbot2.csat.domain.model

data class CsatModel(
    var title: String = "",
    var points: MutableList<PointModel> = mutableListOf(),
    var selectedPoint: PointModel = PointModel(),
    var otherReason: String = ""
)

data class PointModel(
    var score: Int = 0,
    var caption: String = "",
    var reasonTitle: String = "",
    var reasons: List<String> = emptyList(),
    var selectedReasons: List<String> = emptyList(),
    var otherReasonTitle: String = ""
)

val dummyData = CsatModel(
    title = "Gimana pengalamanmu dengan Toko Care?",
    points = mutableListOf(
        PointModel(
            score = 1,
            caption = "Sangat Tidak Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointModel(
            score = 2,
            caption = "Tidak Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointModel(
            score = 3,
            caption = "Kurang Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?",
        ),
        PointModel(
            score = 4,
            caption = "Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointModel(
            score = 5,
            caption = "Sangat Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        )
    ),
    selectedPoint = PointModel(
        score = 4,
        caption = "Puas",
        reasonTitle = "Apa yang menurut kamu kurang?",
        reasons = listOf(
            "Tokopedia Care susah diakses",
            "Jawaban TANYA (Virtual Assistant) tidak membantu"
        ),
        otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
    )
)
