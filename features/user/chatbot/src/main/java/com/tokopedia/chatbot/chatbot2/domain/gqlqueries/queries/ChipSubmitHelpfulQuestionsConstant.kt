package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

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
