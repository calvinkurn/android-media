package com.tokopedia.kol.feature.comment.data.raw

const val GQL_MUTATION_DELETE_KOL_COMMENT: String = "mutation DeleteKolComment(\$idComment: Int!) {\n" +
        "\tdelete_comment_kol(idComment: \$idComment) {\n" +
        "\t\t__typename\n" +
        "\t\terror\n" +
        "\t\tdata {\n" +
        "\t\t\t__typename\n" +
        "\t\t\tsuccess\n" +
        "\t\t\t}\n" +
        "\t}\n" +
        "}"