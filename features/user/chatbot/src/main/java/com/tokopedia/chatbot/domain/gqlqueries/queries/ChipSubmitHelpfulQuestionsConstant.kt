package com.tokopedia.chatbot.domain.gqlqueries

const val CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY = """mutation chipSubmitHelpfulQuestions(${'$'}input: SubmitHelpfulQuestionsParam!) {
  chipSubmitHelpfulQuestions(input: ${'$'}input) {
    status
    serverProcessTime
    data {
    isSuccess
    }
    messageError
  }
}"""