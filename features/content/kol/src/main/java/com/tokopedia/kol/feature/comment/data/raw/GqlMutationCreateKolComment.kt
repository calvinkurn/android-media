package com.tokopedia.kol.feature.comment.data.raw

const val GQL_MUTATION_CREATE_KOL_COMMENT: String = "mutation CreateKolComment(\$idPost: Int!, \$comment: String) {\n" +
        "\tcreate_comment_kol(idPost: \$idPost, comment: \$comment) {\n" +
        "\t\t__typename\n" +
        "\t\terror\n" +
        "\t\tdata {\n" +
        "\t\t\t__typename\n" +
        "\t\t\tid\n" +
        "\t\t\tuser {\n" +
        "\t\t\t\t__typename\n" +
        "\t\t\t\tiskol\n" +
        "\t\t\t\tid\n" +
        "\t\t\t\tname\n" +
        "\t\t\t\tphoto\n" +
        "\t\t\t}\n" +
        "\t\t\tcomment\n" +
        "\t\t\tcreate_time\n" +
        "\t\t}\n" +
        "\t}\n" +
        "}"