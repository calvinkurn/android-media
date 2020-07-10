package com.tokopedia.common_category.data.raw

const val GQL_NAV_SEARCH_PRODUCT: String = "query SearchProduct(\$params: String) {\n" +
        "  searchProduct(params: \$params) {\n" +
        "    source\n" +
        "    totalData: count\n" +
        "    count_text\n" +
        "    additionalParams: additional_params\n" +
        "    errorMessage\n" +
        "    lite_url\n" +
        "    redirection {\n" +
        "      redirectionURL: redirect_url\n" +
        "      departmentID: department_id\n" +
        "    }\n" +
        "    suggestion {\n" +
        "      suggestion\n" +
        "      suggestionCount\n" +
        "      currentKeyword\n" +
        "      instead\n" +
        "      insteadCount\n" +
        "      suggestionText: text\n" +
        "      suggestionTextQuery: query\n" +
        "    }\n" +
        "    related {\n" +
        "      relatedKeyword: related_keyword\n" +
        "      otherRelated: other_related {\n" +
        "        keyword\n" +
        "        url\n" +
        "        applink\n" +
        "      }\n" +
        "    }\n" +
        "    isQuerySafe\n" +
        "    catalogs {\n" +
        "      id\n" +
        "      name\n" +
        "      price\n" +
        "      priceMin: price_min\n" +
        "      priceMax: price_max\n" +
        "      countProduct: count_product\n" +
        "      description\n" +
        "      imageURL: image_url\n" +
        "      url\n" +
        "      category: department_id\n" +
        "    }\n" +
        "    products {\n" +
        "      id\n" +
        "      name\n" +
        "      childs\n" +
        "      url\n" +
        "      imageURL: image_url\n" +
        "      imageURL300: image_url_300\n" +
        "      imageURL500: image_url_500\n" +
        "      imageURL700: image_url_700\n" +
        "      price\n" +
        "      priceRange: price_range\n" +
        "      category: department_id\n" +
        "      categoryID: category_id\n" +
        "      categoryName: category_name\n" +
        "      categoryBreadcrumb: category_breadcrumb\n" +
        "      discountPercentage: discount_percentage\n" +
        "      originalPrice: original_price\n" +
        "      shop {\n" +
        "        id\n" +
        "        name\n" +
        "        url\n" +
        "        isPowerBadge: is_power_badge\n" +
        "        isOfficial: is_official\n" +
        "        location\n" +
        "        city\n" +
        "        reputation\n" +
        "        clover\n" +
        "      }\n" +
        "      wholesalePrice: whole_sale_price {\n" +
        "        quantityMin: quantity_min\n" +
        "        quantityMax: quantity_max\n" +
        "        price\n" +
        "      }\n" +
        "      courierCount: courier_count\n" +
        "      condition\n" +
        "      labels {\n" +
        "        title\n" +
        "        color\n" +
        "      }\n" +
        "      labelGroups: label_groups {\n" +
        "        position\n" +
        "        type\n" +
        "        title\n" +
        "      }\n" +
        "      badges {\n" +
        "        title\n" +
        "        imageURL: image_url\n" +
        "        show\n" +
        "      }\n" +
        "      isFeatured: is_featured\n" +
        "      rating\n" +
        "      countReview: count_review\n" +
        "      stock\n" +
        "      GAKey: ga_key\n" +
        "      preorder: is_preorder\n" +
        "      wishlist\n" +
        "      free_ongkir{\n" +
        "        is_active\n" +
        "        img_url\n" +
        "      }\n" +
        "      shop {\n" +
        "        id\n" +
        "        name\n" +
        "        url\n" +
        "        goldmerchant: is_power_badge\n" +
        "        location\n" +
        "        city\n" +
        "        reputation\n" +
        "        clover\n" +
        "        official: is_official\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n"