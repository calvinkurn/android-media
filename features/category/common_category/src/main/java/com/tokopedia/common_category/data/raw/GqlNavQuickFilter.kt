package com.tokopedia.common_category.data.raw

const val GQL_NAV_QUICK_FILTER: String = "query DynamicAttributes(\$source: String, \$q: String, \$filter: DAFilterQueryType) {\n" +
        "  dynamicAttribute(source: \$source, q: \$q, filter: \$filter) {\n" +
        "    data {\n" +
        "      filter {\n" +
        "        title\n" +
        "        template_name\n" +
        "        search {\n" +
        "          searchable\n" +
        "          placeholder\n" +
        "        }\n" +
        "        options {\n" +
        "          name\n" +
        "          key\n" +
        "          icon\n" +
        "          value\n" +
        "          inputType\n" +
        "          totalData\n" +
        "          valMax\n" +
        "          valMin\n" +
        "          hexColor\n" +
        "          child {\n" +
        "            key\n" +
        "            value\n" +
        "            name\n" +
        "            icon\n" +
        "            inputType\n" +
        "            totalData\n" +
        "            child {\n" +
        "              key\n" +
        "              value\n" +
        "              name\n" +
        "            }\n" +
        "          }\n" +
        "          isPopular\n" +
        "          isNew\n" +
        "          Description\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n"