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
| Product Manager | @Fauzan Ramadhanu @Nevin Jonaputra Liauw                                                                                            |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1225571771/Android+Minion+Bob)                                                   |
| Module type     | <!--start status:Yellow-->FEATURE<!--end status-->                                                                                                 |
| Module Location | <code>features/transaction/minicart</code>                                                                                                            |
| Product PRD     | [PRD OCC Multi - Tokopedia Now](https://docs.google.com/document/d/1xgPIQkocPZoP0HATcvvbC4M8kgzkV1gVJvAU8vfhONQ/edit?pli=1#heading=h.1rxfwnt6h4rr) |

<!--toc-->
## Release Notes

## Overview
Mini Cart V2 Widget is a new Mini Cart Widget, which only handles total amount section of mini cart. This new widget enables better separation & easier integration.

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
You need to initialize MiniCartV2Widget by calling `initialize()` on your page with following parameters :

config: MiniCartV2WidgetConfig [Mandatory]

This is a configuration of the Mini Cart V2 Widget

listener: MiniCartWidgetListener [Mandatory]

This is a listener which will be used for notify the holder page, containing these callbacks:

- onCartItemsUpdated [Mandatory]

   this callback will be called whenever mini cart data is updated 
- onFailedToLoadMiniCartWidget
  
   this callback will be called whenever mini cart failed to get data from BE
- onChevronClickListener

  this callback will be called whenever user click chevron
- getFragmentManager

  this callback will be called when mini cart want to open global error bottomsheet. This is required if the mini cart primary action is redirection to checkout/occ page
- onPrimaryButtonClickListener

  this callback will be called whenever user click primary button & mini cart config override primary button action is true
- onAdditionalButtonClickListener

  this callback will be called whenever user click additional button
- onFailedToGoToCheckoutPage

  this callback will be called whenever mini cart failed to go to checkout page. Dev are expected to trigger refresh mini cart

Public Method
- refresh(param: GetMiniCartParam)

   This method will trigger widget to update the data from backend based on provided param.

- refresh(miniCartSimplifiedData: MiniCartSimplifiedData)

   This method will trigger widget to update the data from provided parameter  MiniCartSimplifiedData. More about MiniCartSimplifiedData, please check this documentation.

- showLoading()

   This method will trigger to show Mini Cart V2 Widget Loading State. To hide the loading, dev must use any refresh function stated above.

## Useful Links
- [GQL Mini Cart](https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1477936471)
- [Mini Cart Draw](https://www.tldraw.com/r/kNG3lrmuuRLkn7v9f7Psl?viewport=2202,347,981,482&page=page:page)

### See Also
- [ATC Common](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1428718668) → To add product(s) to OCC
- [Cart-Checkout Localized Chosen Address Logic | OCC Page](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1858210131/Cart-Checkout+Localized+Chosen+Address+Logic#OCC-Page) → About Localized Address handling
- [Checkout](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1426720585) → About regular checkout flow

### Tech Plans
- [GWP - Gift With Purchase](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2434008306/WIP+GWP+-+Gift+With+Purchase)

## FAQ