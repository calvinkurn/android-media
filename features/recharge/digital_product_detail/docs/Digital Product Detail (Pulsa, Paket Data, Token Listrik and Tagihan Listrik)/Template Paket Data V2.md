---
title: "Template Paket Data V2"
labels:
- telco
- mark
- digital
- main-app
- revampv2
- pdp
- android
- paket-data
- prepaid
- recharge
---
## Table of Contents

<!--toc-->

## Overview

The new PDP now has multiple widgets available to be inserted to the page. Beside the components you can see in the screenshot below, there are some other widgets such as:

- Last Transaction
- [MCCM](/wiki/spaces/PA/pages/1788913609/Recharge+MCCM) (Multi Channel Customer Management)
- Telkomsel Omni Widget

![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.33.35.png)

## How-to

### Applink

`tokopedia://digital/form?category_id=2&menu_id=290&template=paketdatav2`

### APIs



| **API Query Name** | **Type** | **Usage / Notes** | **Cache** |
| --- | --- | --- | --- |
| `rechargeCatalogMenuDetail` | `GQL` | Get menu detail such as page detail, ticker | 5 mins |
| `rechargeCatalogPrefixSelect` | `GQL` | Get prefixes, regex validators, operatorId, and icons. | 10 mins |
| `rechargeCatalogProductInputMultiTab` | `GQL` | Get products and MCCM data | - |
| `digiPersoGetPersonalizedItems` | `GQL` | Get favorite number (list, chip, prefill, recommendation, telkomsel omni widget) for existing user. each has different channel | - |
| `rechargeAddToCartV2` | `GQL` | for add to cart purposes. Response will be passed on to digital checkout page. | - |
| `cart` | `REST` | for add to cart purposes. Response will be passed on to digital checkout page. | - |

### User Journey



| **User Journey** | **Tech Flow** | **Screenshot** |
| --- | --- | --- |
| User open the Paket Data page | 1. load data from bundle (menuId, etc). If there’s client number then put it in client number field (autofill)<br/>2. load initial data from<br/>	1. `rechargeCatalogMenuDetail` for page detail, ticker<br/>	2. `rechargeCatalogPrefixSelect` for operator data, regex validator<br/>	3. `digiPersoGetPersonalizedItems` for prefill, chips, and autocomplete data<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.37.38.png)<br/> |
| User type valid client number in input field | 1. if operatorData is not exists yet, call `getPrefixOperatorData()` which will hit `rechargeCatalogPrefixSelect`<br/>2. search the operatorData that match with prefix input by user, for example `0812`:<br/>	1. if found, you’ll get Telkomsel operatorData.<br/>	2. if not, throw error<br/>3. validate the client number using regex validator from `rechargeCatalogPrefixSelect`<br/>4. check if the input number operatorId is same with current operatorId (isOperatorChanged)<br/>5. set default productId<br/>6. check if the input number is in range of 10-14 (valid client number):<br/>	1. if valid || isOperatorChanged:<br/>		1. set operator & operator icon<br/>		2. get recommendation (digiPerso)<br/>		3. get product (multiTab)<br/>		4. show buy widget<br/>	2. if not:<br/>		1. hide buy widget<br/>7. show product<br/>8. auto select product if meet the criteria (`DigitalPDPDataPlanViewModel.isAutoSelectedProduct`)<br/><br/>in parallel:1. show loading that’s triggered from client number validation (step 3) for 1 sec:<br/>	1. if error, show the error after the loading ends<br/>	2. if not error, hide error state<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.39.47.png)<br/> |
| User input invalid client number (unidentified prefix) | 1. if operatorData is not exists yet, call `getPrefixOperatorData()` which will hit `rechargeCatalogPrefixSelect`<br/>2. search the operatorData that match with prefix input by user<br/>	1. if found, you’ll get Telkomsel operatorData.<br/>	2. if not, throw error<br/>3. catch error, reset operator, reset contact name, show error message, show empty state<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.40.28.png)<br/> |
| user click favorite number chip | 1. Fill the input number field with the selected favorite number, and label with name (only if user already changed the name in favorite number page)<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.42.22.png)<br/> |
| user click phone number icon | 1. If user haven’t given contact permission, show pop up dialog asking for the permission<br/>2. Navigate to favorite number page:<br/>	1. If user choose allow, they’ll be able to see their contact number in favorite number page<br/>	2. if not, there’ll be empty state with ATC to phone settings<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.43.42.png)<br/> |
| user click burger icon | 1. If user haven’t given contact permission, show pop up dialog asking for the permission<br/>2. Navigate to favorite number page, auto selected to “Nomor Favorit” tab.<br/>3. After user choose the number, autofill the selected number into input field, and name into input field label<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.44.40.png)<br/> |
| User click product card | 1. check if the product is not selected:<br/>	1. if not selected:<br/>		1. highlight the product card<br/>		2. update productId and buyWidget<br/>	2. if selected:<br/>		1. remove highlight (unselect product)<br/>		2. remove productId<br/>		3. hide buyWidget<br/> |  ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.45.24.png)<br/> |
| User click MCCM card | 1. check if the product is not selected:<br/>	1. if not selected:<br/>		1. highlight the product card<br/>		2. update productId and buyWidget<br/>	2. if selected:<br/>		1. remove highlight (unselect product)<br/>		2. remove productId<br/>		3. hide buyWidget<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.46.44.png)<br/> |
| User click recommendation card | 1. check if user is login:<br/>	1. is login:<br/>		1. navigate to checkout page using applink associated to the card<br/>	2. is not login:<br/>		1. redirect user to login page<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.45.38.png)<br/> |
| User click chevron in buy widget | 1. show Ringkasan Pembayaran bottom sheet with detailed price<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2016.36.05.png)<br/> ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2016.36.59.png)<br/> |
| User click `Lanjutkan` button | 1. update checkoutPassData (`viewModel.updateCheckoutPassData(...))`<br/>2. check if user is login:<br/>	1. is login:<br/>		1. call ATC<br/>		2. if ATC success navigate to checkout page<br/>		3. if ATC is not success show error toaster<br/>	2. is not login:<br/>		1. redirect user to login page<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2016.36.05.png)<br/> |
| User click Filter Icon and select any filter | 1. Open filter bottom sheet<br/>2. If user click one of the chip, it’ll be selected and highlighted. User can choose more than 2 filter<br/>3. If user click simpan, user will be navigated back to Paket Data PDP and the chosen filter will be applied<br/>4. If user click semua, bottom sheet of selected filter cluster will be shown<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.47.49.png)<br/>![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.48.05.png)<br/> |
| User click Filter icon shortcut | 1. Apply the selected filter to the product list immediately<br/> | ![](../res/paketdatav2/Screen%20Shot%202023-03-20%20at%2017.50.10.png)<br/> |

## Testing



| **Test** | **Class Name** | **Notes** |
| --- | --- | --- |
| Unit Test | `DigitalPDPDataPlanViewModelTest``DigitalPDPDataPlanViewModelTestFixture` | We use `Fixture` to contain all mock, validation/assertion logics so it’ll be more readable in the actual test class |
| Instrumentation Test | `BaseDigitalPDPDataPlanTest``DigitalPDPDataPlanCassavaTest` | We use `Base` as an abstract class to contain all espresso logics, and we expect there’ll be `CassavaTest` and `UiTest` for each PDP `Base`.Local Cassava`digital_pdp_dataplan.json` |

## Useful Links

- [Figma](https://www.figma.com/file/O2ztGn39sOaZFn7kCTFWTR/%5BUI---M%5D-DG-PDP-Revamp-Q4-2021?t=W4vEmNbDVT2exSDY-6)
- [Tracker](https://mynakama.tokopedia.com/datatracker/requestdetail/view/2500)
