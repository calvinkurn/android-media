---
title: "Mini Cart V2 Widget"
labels:
- minicart
- purchase-platform
- android
- cart
---

---
<!--left header table-->
| **Status**      | <!--start status:Green-->RELEASE<!--end status-->                                                                                                  |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Product Manager | @Fauzan Ramadhanu @Nevin Jonaputra Liauw @Ayesha Brenda                                                                                            |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1225571771/Android+Minion+Bob)                                                   |
| Module type     | <!--start status:Yellow-->FEATURE<!--end status-->                                                                                                 |
| Module Location | <code>features/transaction/minicart</code>                                                                                                            |
| Product PRD     | [PRD OCC Multi - Tokopedia Now](https://docs.google.com/document/d/1xgPIQkocPZoP0HATcvvbC4M8kgzkV1gVJvAU8vfhONQ/edit?pli=1#heading=h.1rxfwnt6h4rr) |

<!--toc-->
## Release Notes
<!--start expand:17 Feb (MA-3.210)-->
[OCC - GoPayLater Cicil 0% Promo](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2146763827)
<!--end expand-->

## Overview
![OCC page/OSP](https://docs-android.tokopedia.net/images/docs/oneclickcheckout/overview_occ.png)

One Click Checkout (OCC) aka Beli Langsung is a “quick buy” process for Marketplace products. Using OCC, user will be able to directly checkout without having to navigate through Cart page & Checkout page (aka normal buy process).

The purpose of OCC is to reduce the number of clicks and duration of the whole checkout process, by providing user with a single summary page before checking out the product, called Order Summary Page (OSP)

## Tech Stack
- Coroutines
- MVVM

## How To
1. Add dependencies  
   Add Mini Cart, Cart Common and Localization Choose Address Common dependency to your module’s build.gradle
```
implementation projectOrAar(rootProject.ext.features.minicart)
implementation projectOrAar(rootProject.ext.features.cart_common)
implementation projectOrAar(rootProject.ext.features.localizationchooseaddress_common)
```
2. Implement MiniCartV2Widget
   Add MiniCartV2Widget to your page’s layout

3. Initialize MiniCartV2Widget
    ```
   miniCartV2Widget.initialize(
                MiniCartV2Widget.MiniCartV2WidgetConfig(
                    ...
                ),
                getMiniCartV2WidgetListener()
            )
   ```
You need to initialize MiniCartV2Widget by calling initialize() on your fragment with following parameters :

config: MiniCartV2WidgetConfig [Mandatory]

This is a configuration of the Mini Cart V2 Widget

listener: MiniCartWidgetListener [Mandatory]

This is a listener which will be used for notify the holder page, containing these callbacks:

- onCartItemsUpdated [Mandatory]

Public Method
updateData(shopIds: List<String>)

This method will trigger widget to update the data from backend based on provided shopIds.

updateData(miniCartSimplifiedData: MiniCartSimplifiedData)

This method will trigger widget to update the data from provided parameter  MiniCartSimplifiedData. More about MiniCartSimplifiedData, please check this documentation.

showMiniCartListBottomSheet(fragment: Fragment)

This method will trigger to show Mini Cart Bottom Sheet. The example case is when user click CTA Lihat on success toaster after ATC product on a page that implement mini cart widget and need to show mini cart bottom sheet.

## Useful Links
- [PRD OCC Multi - Tokopedia Now](https://docs.google.com/document/d/1xgPIQkocPZoP0HATcvvbC4M8kgzkV1gVJvAU8vfhONQ/edit?pli=1#heading=h.1rxfwnt6h4rr)
- [Figma OCC Multi - Tokopedia Now](https://www.figma.com/file/BdMdmdfURvDmlHpx8r6pe8/UX%2FUI---Tokopedia-NOW!-%5BGrand-Launch---August%5D?node-id=1166%3A0)

### GQL List
#### GQL Docs

| **GQL Name**         | **Team**          | **Usage**                                             | **Link**                                                                                                       |
|----------------------|-------------------|-------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| GetOCCMulti          | PP - Cart         | Get Cart Data                                         | [[GraphQL] Cart Page Multi](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1602814548)                  |
| UpdateCartOCCMulti   | PP - Cart         | Update Cart Data                                      | [[GraphQL] Update Cart OCC Multi](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1603142893)            |
| RatesV3              | Logistic          | Get Shipping Data                                     | [Rates V3](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/567279712)                                     |
| OneClickCheckout     | PP - Checkout     | Checkout                                              | [[Mutation] One Click Checkout](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/724043954)               |
| ValidateUse          | Promo & PP - Cart | Apply Promo & Get Benefit                             | [[GraphQL] Promo Revamp Validate Promo Use](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/706323972)   |
| KeroAddressCorner    | Address           | Get Address List                                      | LogisticCommon Module                                                                                          |
| FetchInstantTopupUrl | Payment           | Get OVO TopUp URL                                     | [GQL Fetch Instant Top Up URL](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/736134467)                 |
| GetListingParams     | Payment           | Get Payment Params For Loading WebView                | -                                                                                                              |
| CreditCardTenorList  | Payment           | Get Tenor List For CC & Debit Card                    | [Credit Card Tenor List API for Merchant Use](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/1521485348) |
| GetInstallmentInfo   | Payment           | Get Installment Options For GoPayLater Cicil          | [Gopaylater Cicil - Installment Option](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/1894089469)       |
| GetPaymentFee        | Payment           | Get Dynamic Payment Fee, especially for platform fee  | [GQL Get Payment Fee](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/2106034126)                         |

#### API Docs
| **API Name**            | **Team**          | **Usage**                 | **Link**                                                                                                          |
|-------------------------|-------------------|---------------------------|-------------------------------------------------------------------------------------------------------------------|
| GetOCCMulti             | PP - Cart         | Get Cart Data             | [[External] Cart Page One Click Checkout Multi](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1589971576) |
| UpdateCartOCCMulti      | PP - Cart         | Update Cart Data          | [[External] Update Cart OCC Multi](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1602814277)              |
| RatesV3                 | Logistic          | Get Shipping Data         | [Rates V3](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/567279712)                                        |
| OneClickCheckout        | PP - Checkout     | Checkout                  | [[Contract] 1Click Checkout API Documentation](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/699572176)   |
| ValidateUse             | Promo & PP - Cart | Apply Promo & Get Benefit | [[External] Promo Revamp Validate Promo Use](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/706323972)     |
| Payment Listing WebView | Payment           | Open Payment List Page    | [Open Gateway Settings Page](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/699640342)                      |
| CC List WebView         | Payment           | Open CC List Page         | [Show Card List API](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/861243905)                              |
| OVO Activation WebView  | Payment           | Open OVO Activation Page  | [Activation](https://tokopedia.atlassian.net/wiki/spaces/PY/pages/517442003)                                      |

### See Also
- [ATC Common](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1428718668) → To add product(s) to OCC
- [Cart-Checkout Localized Chosen Address Logic | OCC Page](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1858210131/Cart-Checkout+Localized+Chosen+Address+Logic#OCC-Page) → About Localized Address handling
- [Promo Checkout Marketplace](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1426100871) → About promo in marketplace
- [Checkout](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1426720585) → About regular checkout flow

### Tech Plans
- [OCC - BNPL](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1909556577)
- [Bebas Ongkir Unstack | OSP](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2003501967/Bebas+Ongkir+Unstack#OSP)
- [OCC - Epharmacy](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2065990815)
- [Whitelabel Instan](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2104460915)
- [OCC - Dynamic Platform Fee](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2109181348)
- [OCC - GoPayLater Cicil 0% Promo](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2146763827)

## FAQ