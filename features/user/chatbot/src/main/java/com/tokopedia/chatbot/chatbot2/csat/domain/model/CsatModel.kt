package com.tokopedia.chatbot.chatbot2.csat.domain.model

data class CsatModel(
    var title: String = "",
    var minimumOtherReasonChar: Int = 0,
    var maximumOtherReasonChar: Int = 0,
    var points: MutableList<PointModel> = mutableListOf(),
    var selectedPoint: PointModel = PointModel(),
    var selectedReasons: MutableList<String> = mutableListOf(),
    var otherReason: String = ""
)

data class PointModel(
    var score: Int = 0,
    var caption: String = "",
    var reasonTitle: String = "",
    var reasons: List<String> = emptyList(),
    var otherReasonTitle: String = ""
)

data class SubmitButtonState(
    var isEnabled: Boolean = false
)

var dummyData = CsatModel(
    title = "Gimana pengalamanmu dengan Tokopedia Care?",
    minimumOtherReasonChar = 30,
    maximumOtherReasonChar = 1500,
    points = mutableListOf(
        PointModel(
            score = 1,
            caption = "Sangat Tidak Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Reason 1",
                "Reason 11",
                "Reason 111",
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
                "Reason 2",
                "Reason 22",
                "Reason 222",
                "Reason 2",
                "Reason 22",
                "Reason 222",
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
                "Reason 3",
                "Reason 33",
                "Reason 333",
                "Reason 3",
                "Reason 33",
                "Reason 333",
                "Reason 3",
                "Reason 33",
                "Reason 333",
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        ),
        PointModel(
            score = 4,
            caption = "Puas",
            reasonTitle = "Apa yang menurut kamu kurang?",
            reasons = listOf(
                "Reason 4",
                "Reason 44",
                "Reason 444",
                "Reason 4444",
                "Reason 44444",
                "Reason 444444",
                "Reason 4444444",
                "Reason 44444444",
                "Reason 444444444",
                "Reason 4444444444",
                "Reason 44444444444",
                "Reason 444444444444",
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
                "Reason 5",
                "Reason 55",
                "Reason 555",
                "Reason 5",
                "Reason 55",
                "Reason 555",
                "Reason 5",
                "Reason 55",
                "Reason 555",
                "Reason 5",
                "Reason 55",
                "Reason 555",
                "Tokopedia Care susah diakses",
                "Jawaban TANYA (Virtual Assistant) tidak membantu"
            ),
            otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
        )
    ),
    selectedPoint = PointModel(
        score = 1,
        caption = "Sangat Tidak Puas",
        reasonTitle = "Apa yang menurut kamu kurang?",
        reasons = listOf(
            "Reason 1",
            "Reason 11",
            "Reason 111",
            "Tokopedia Care susah diakses",
            "Jawaban TANYA (Virtual Assistant) tidak membantu"
        ),
        otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
    )
)
