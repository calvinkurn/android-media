---
title: "Digital Product Detail (Pulsa, Paket Data, Token Listrik and Tagihan Listrik)"
labels:
- digital
- pdp
- main-app
- revampv2
- recharge
- android
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors |  [Firmanda Mulyawan Nugroho](https://tokopedia.atlassian.net/wiki/people/5d91c148fdfa560dcc3a040f?ref=confluence) [Misael Jonathan](https://tokopedia.atlassian.net/wiki/people/60051d42e64c95006fbaad73?ref=confluence)  |
| Product Manager | [Kevin Winarya (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5cb561663704fd1181fecb96?ref=confluence) [Muhammad Ardhan Fadhlurrahman (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/61cb95349ee70a006809956c?ref=confluence)  |
| Team | [Minion Mark](https://tokopedia.atlassian.net/people/team/54372146-8afa-46e4-8de3-783c53a0cc3b) |
| Release Version | <!--start status:GREY-->VERSION 3.167<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | [Digital Goods - PDP Revamp [Pulsa, Paket Data, Listrik PLN & Uang Elektronik]](/wiki/spaces/~632226527/pages/1843758954) [PDP Revamp [Token Listrik]](/wiki/spaces/~632226527/pages/1885508696) [PDP Revamp [ Paket Data ]](/wiki/spaces/~632226527/pages/1885613330)  |
| Module Location | `features/recharge/recharge_general` |

## Table of Contents

<!--toc-->

## Overview

Revamped Digital Product Detail development happened in Q1 2022 and was already released in 3.167 Android Customer App. Revamp Digital Product Detail includes new Pulsa Page, Paket Data Page, Token Listrik Page, and Tagihan Listrik page replacing the old page that is responsible for these features. You can find these pages in the new module `digital_product_detail`. All control for the pages that showing to the user is on the Business side, so currently, only Pulsa and Paket Data pages that already shown to the user but Token Listrik and Tagihan are not yet shown to the user.

### Background

We want to provide a seamless user experience to make customers comfortable to navigate, with minimal learning curve by unifying the product experience across different categories.

## Flow Diagram

### Architecture

As you can see below each page has a different page to make the separate concern and to make scalability easier for each page. But on the domain site, we can reuse the repository using a delegate pattern.

![](res/Digital%20PDP%20Revamp.jpg)

## How-to

### GraphQL Data



| `GetRechargeInquiryUseCase` **→** `RechargeInquiryRepository` |
| --- |
| <br/><br/>```<br/>query enquiry (${'$'}fields: [RechargeKeyValue]!) {<br/>  status<br/>  rechargeInquiry(fields:${'$'}fields) {<br/>    status: Status<br/>    retrySec: RetrySec<br/>    attributes: Attributes {<br/>      UserID<br/>      ProductID<br/>      ClientNumber<br/>      Title<br/>      Price<br/>      PricePlain<br/>      mainInfo: MainInfo {<br/>        label: Label<br/>        value: Value<br/>      }<br/>      additionalInfo: AdditionalInfo{<br/>        title: Title<br/>       detail: Detail {<br/>          label: Label<br/>         value: Value<br/>       }<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



| `GetRechargeCatalogOperatorSelectGroupUseCase` **→** `RechargeCatalogOperatorSelectGroupRepository` |
| --- |
| <br/><br/>```<br/>query catalogOperatorSelectGroup(${'$'}menuID: Int!) {<br/>  rechargeCatalogOperatorSelectGroup(menuID:${'$'}menuID, platformID: 5){<br/>    text<br/>    style<br/>    help<br/>    validations {<br/>      id<br/>      title<br/>      rule<br/>      message<br/>    }<br/>    operatorGroup{<br/>      name<br/>      operators{<br/>        type<br/>        id<br/>        attributes{<br/>          name<br/>          image<br/>          image_url<br/>          prefix<br/>          operator_descriptions<br/>          default_product_id<br/>          custom_attributes {<br/>            id<br/>          }<br/>          custom_data {<br/>              tabs {<br/>              name<br/>              value<br/>          }<br/>         }<br/>        }<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



| `GetRechargeRecommendationUseCase` -> `RechargeRecommendationRepository` |
| --- |
| <br/><br/>```<br/>query digiPersoGetPersonalizedItems(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {<br/>  digiPersoGetPersonalizedItems(input: ${'$'}input) {<br/>    title<br/>    mediaURL<br/>    appLink<br/>    webLink<br/>    textLink<br/>    items {<br/>      id<br/>      title<br/>      mediaURL<br/>      mediaUrlType<br/>      appLink<br/>      webLink<br/>      label1<br/>      label2<br/>      price<br/>      slashedPrice<br/>      discount<br/>      backgroundColor<br/>      trackingData {<br/>        productID<br/>        operatorID<br/>        businessUnit<br/>        itemLabel<br/>        itemType<br/>        clientNumber<br/>        categoryID<br/>        categoryName<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



| `GetRechargeCatalogInputMultiTabUseCase` → `RechargeCatalogProductInputMultiTabRepository` |
| --- |
| <br/><br/>```<br/>query telcoProductMultiTab(${'$'}menuID: Int!,${'$'}operatorID: String!,${'$'}filterData: [RechargeCatalogFilterData], ${'$'}clientNumber: [String]) {<br/>  rechargeCatalogProductInputMultiTab(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operatorID, filterData:${'$'}filterData, clientNumber:${'$'}clientNumber) {<br/>    productInputs {<br/>      label<br/>      needEnquiry<br/>      isShowingProduct<br/>      enquiryFields {<br/>        id<br/>        param_name<br/>        name<br/>      }<br/>      product {<br/>        id<br/>        name<br/>        text<br/>        placeholder<br/>        validations {<br/>          rule<br/>        }<br/>        dataCollections {<br/>          name<br/>          cluster_type<br/>          products {<br/>            id<br/>            attributes {<br/>              status  <br/>              product_labels<br/>              desc<br/>              detail<br/>              detail_url<br/>              detail_url_text<br/>              info<br/>              price<br/>              price_plain<br/>              status<br/>              detail_compact<br/>              category_id<br/>              operator_id<br/>              product_descriptions<br/>              custom_attributes {<br/>                id<br/>                name<br/>                value<br/>              }<br/>              promo {<br/>                id<br/>                bonus_text<br/>                new_price<br/>                new_price_plain<br/>                value_text<br/>                discount<br/>              }<br/>            }<br/>          }<br/>        }<br/>      }<br/>      filterTagComponents {<br/>        name<br/>        text<br/>        param_name<br/>        data_collections {<br/>          key<br/>          value<br/>        }<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



| `DigitalAddToCartRestUseCase` → `DigitalAddToCartUseCase` → `RechargeAddToCartRepository` |
|-------------------------------------------------------------------------------------------|
| `Using Rest` `https://pulsa-api.tokopedia.com/v1.4/cart`                                  |



| `RechargeAddToCartGqlUseCase` → `DigitalAddToCartUseCase` → `RechargeAddToCartRepository`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <br/><br/>```<br/>mutation rechargeAddToCartV2(${'$'}request:RechargeAddToCartRequestV2!) {<br/>  rechargeAddToCartV2(body: ${'$'}request) {<br/>    data {<br/>      type<br/>      id<br/>      attributes {<br/>        user_id<br/>        client_number<br/>        title<br/>        category_name<br/>        operator_name<br/>        icon<br/>        price<br/>        price_plain<br/>        instant_checkout<br/>        need_otp<br/>        sms_state<br/>        voucher_autocode<br/>        user_input_price<br/>        user_open_payment {<br/>          min_payment_text<br/>          max_payment_text<br/>          min_payment<br/>          max_payment<br/>          max_payment_error_text<br/>          min_payment_error_text<br/>        }<br/>        enable_voucher<br/>        is_coupon_active<br/>        default_promo_dialog_tab<br/>        main_info {<br/>          label<br/>          value<br/>        }<br/>      }<br/>    }<br/>    errors {<br/>      status<br/>      title<br/>      applink_url<br/>      atc_error_page{<br/>        show_error_page<br/>        title<br/>        sub_title<br/>        image_url<br/>        buttons{<br/>          label<br/>          url<br/>          applink_url<br/>          action_type<br/>        }<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



|  `GetRechargeCatalogMenuDetailUseCase` → `RechargeCatalogMenuDetailRepository` |
| --- |
| <br/><br/>```<br/>query catalogMenuDetail(${'$'}menuID: Int!){<br/>  rechargeCatalogMenuDetail(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID) {<br/>    catalog {<br/>      id<br/>      name<br/>      label<br/>      icon<br/>    }<br/>    user_perso {<br/>        prefill<br/>        client_name<br/>        user_type<br/>        loyalty_status<br/>        prefill_operator_id<br/>    }<br/>    recommendations {<br/>      iconUrl<br/>      title<br/>      clientNumber<br/>      appLink<br/>      webLink<br/>      productPrice<br/>      type<br/>      categoryId<br/>      productId<br/>      isATC<br/>      description<br/>      operatorID<br/>    }<br/>    promos {<br/>      id<br/>      filename<br/>      filename_webp<br/>      img_url<br/>      status<br/>      title<br/>      subtitle<br/>      promo_code<br/>    }<br/>    tickers {<br/>      ID<br/>      Name<br/>      Content<br/>      Type<br/>      Environment<br/>      ActionText<br/>      ActionLink<br/>    }<br/>    banners {<br/>      id<br/>      title<br/>      img_url: filename<br/>      link_url: img_url<br/>      promo_code<br/>      app_link<br/>    }<br/>    express_checkout<br/>  }<br/>}<br/>```<br/><br/> |



|  `GetRechargeCatalogPrefixSelectUseCase` → `RechargeCatalogPrefixSelectRepository` |
| --- |
| <br/><br/>```<br/>query telcoPrefixSelect(${'$'}menuID: Int!) {<br/>  rechargeCatalogPrefixSelect(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID) {<br/>    componentID<br/>    name<br/>    paramName<br/>    text<br/>    help<br/>    placeholder<br/>    validations {<br/>      id<br/>      title<br/>      message<br/>      rule<br/>    }<br/>    prefixes {<br/>      key<br/>      value<br/>      operator {<br/>        id<br/>        attributes {<br/>          name<br/>          default_product_id<br/>          image_url<br/>        }<br/>      }<br/>    }<br/>  }<br/>}<br/>```<br/><br/> |



|  `GetRechargeFavoriteNumberUseCase` → `RechargeFavoriteNumberRepository` |
| --- |
| <br/><br/>```<br/>query digiPersoGetPersonalizedItems(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {<br/>          digiPersoGetPersonalizedItems(input: ${'$'}input) {<br/>            title<br/>            mediaURL<br/>            appLink<br/>            webLink<br/>            textLink<br/>            items {<br/>              id<br/>              title<br/>              mediaURL<br/>              mediaUrlType<br/>              appLink<br/>              webLink<br/>              label1<br/>              label2<br/>              price<br/>              slashedPrice<br/>              discount<br/>              backgroundColor<br/>              trackingData {<br/>                productID<br/>                operatorID<br/>                businessUnit<br/>                itemLabel<br/>                itemType<br/>                clientNumber<br/>                categoryID<br/>                categoryName<br/>              }<br/>            }<br/>          }<br/>        }<br/>```<br/><br/> |

### Favorite Number Channel

We are using Perso service to get all favorite number related data, and the best part is we don’t have to filter the data in the FE side for different purposes, we only need to show what BE return to us. For example, the data return in the `prefill` should only return one personalized favorite number (it’s up to BE on what criteria they want to return the prefill), and `chips` only contains some of favorite numbers.

Each channel represent different purpose, and have to be sent through `channelName` param in `digiPersoGetPersonalizedItems` GQL. These are the available channels:

- `favorite_number_list` => autocomplete, and favorite number list in favnum page
- `favorite_number_chips` => shortcut chips
- `favorite_number_prefill` => pdp prefill
- `pulsa_pdp_last_transaction` => recommendation / last transaction for Pulsa
- `recharge_pdp_last_trx_client_number` => recommendation / last transaction for Paket Data and Token Listrik

## Navigation

### Applinks Format

`tokopedia://digital/form?category_id={}&menu_id={}&client_number={}&template={}&operator_id={}&product_id={}`



| **Param Name** | **Mandatory** | **Description** | **Example** |
| --- | --- | --- | --- |
| `category_id` | Yes | ID that specify which category you want to open (pascabayar, signal) | `9` |
| `menu_id` | Yes | Identifier for getting menu detail | `3` |
| `template` | Yes | Use `telcopost` value to use voucher game template | `telcopost` |
| `client_number` | No | Use this param to autofill the client number field | `081208120812` |
| `operator_id` | No | Use this param to specify operator. but usually this value will be overridden if we put `client_number` | `18` |
| `product_id` | No | Use this param to autoselect product. usually this param is used together with `client_number` & `operator_id` | `1124` |

## Products V2

[Pulsa](/wiki/spaces/PA/pages/2198537422/Template+Pulsa+V2)

[Paket Data](/wiki/spaces/PA/pages/2199421485/Template+Paket+Data+V2)

[Token Listrik](/wiki/spaces/PA/pages/2198802349/Template+Token+Listrik+V2)

[Tagihan](/wiki/spaces/PA/pages/2198808842/Template+Tagihan+Listrik+V2)

## Useful Links

- [Figma](https://www.figma.com/file/O2ztGn39sOaZFn7kCTFWTR/%5BUI---M%5D-DG-PDP-Revamp-Q4-2021)
