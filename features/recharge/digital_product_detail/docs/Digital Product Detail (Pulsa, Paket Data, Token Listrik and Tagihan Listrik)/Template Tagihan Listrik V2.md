---
title: "Template Tagihan Listrik V2"
labels:
- revampv2
- mark
- pdp
- android
- recharge
- digital
- main-app
- token-listrik
---
## Table of Contents

<!--toc-->

## Overview

The new PDP now has multiple widgets available to be inserted to the page. This PDP is already available in the app, but it’s not live yet.

![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2013.55.11.png)

## How-to

### Applink

`tokopedia://digital/form?category_id=3&menu_id=305&template=tagihanplnv2`

### APIs



| **API Query Name** | **Type** | **Usage / Notes** | **Cache** |
| --- | --- | --- | --- |
| `rechargeCatalogMenuDetail` | `GQL` | Get menu detail such as page detail, ticker | 5 mins |
| `rechargeCatalogProductInputMultiTab` | `GQL` | Get products data | - |
| `rechargeCatalogOperatorSelectGroup` | `GQL` | Get regex validators and operator group data. | - |
| `digiPersoGetPersonalizedItems` | `GQL` | Get favorite number (list, chip, prefill, recommendation) for existing user. each has different channel | - |
| `rechargeInquiry` | `GQL` | Inquiry the bill | - |
| `rechargeAddToCartV2` | `GQL` | for add to cart purposes. Response will be passed on to digital checkout page. | - |
| `cart` | `REST` | for add to cart purposes. Response will be passed on to digital checkout page. | - |

### User Journey



| **User Journey** | **Tech Flow** | **Screenshot** |
| --- | --- | --- |
| User open the Tagihan page | 1. load data from bundle (menuId, etc). If there’s client number then put it in client number field (autofill)<br/>2. load initial data from <br/>	1. `rechargeCatalogMenuDetail` for page detail, ticker<br/>	2. `rechargeCatalogOperatorSelect` for operator group data and regex validator<br/>	3. `digiPersoGetPersonalizedItems` for prefill, chips, and autocomplete data<br/> | ![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.35.40.png)<br/> |
| User type valid client number in input field | 1. if operatorData is not exists yet, call `getOperatorSelectGroup()` which will hit `rechargeCatalogOperatorSelectGroup`<br/>2. check if the input number have reached the minimum operator prefix length (1)<br/>3. validate the client number using regex validator from `rechargeCatalogOperatorSelectGroup`<br/>4. set default productId<br/>5. do all of this:<br/>	1. get recommendation (digiPerso)<br/>	2. get product (multiTab)<br/><br/>in parallel:1. show loading that’s triggered from client number validation (step 3) for 1 sec:<br/>	1. if error, show the error after the loading ends<br/>	2. if not error, hide error state<br/>2. show autocomplete if the input number (as prefix) is match with any favorite number<br/> | ![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.36.01.png)<br/>![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.36.48.png)<br/> |
| User input invalid client number (doesn’t match validator) | 1. if operatorData is not exists yet, call `getOperatorSelectGroup()` which will hit `rechargeCatalogOperatorSelectGroup`<br/>2. validate the client number using regex validator from `rechargeCatalogOperatorSelectGroup`<br/>3. show error message<br/> | ![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.37.10.png)<br/> |
| user click favorite number chip | 1. Fill the input number field with the selected favorite number, and label with name (only if user already changed the name in favorite number page)<br/> | ![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.37.46.png)<br/> |
| user click phone QR code ico | 1. Navigate to favorite number page:<br/>	1. If user choose allow, they’ll be able to see their contact number in favorite number page<br/>	2. if not, there’ll be empty state with ATC to phone settings<br/> | ![](../res/Screen%20Shot%202023-03-21%20at%2014.38.02.png)<br/> |
| user click burger icon | 1. Navigate to favorite number page<br/>2. After user choose the number, autofill the selected number into input field, and name into input field label<br/> | ![](../res/tagihanlistrikv2/Screen%20Shot%202023-03-21%20at%2014.38.24.png)<br/> |
| User click `Lanjut Bayar` button | 1. if eligible to buy (pass the regex validation), proceed, if not start shake animation in the client number widget<br/>2. update checkoutPassData (`viewModel.updateCheckoutPassData(...))`<br/>3. check if user is login:<br/>	1. is login:<br/>		1. call ATC<br/>		2. if ATC success navigate to checkout page<br/>		3. if ATC is not success show error toaster<br/>	2. is not login:<br/>		1. redirect user to login page<br/> | ![](../res/Screen%20Shot%202023-03-21%20at%2014.40.05.png)<br/> |

## Testing



| **Test** | **Class Name** | **Notes** |
| --- | --- | --- |
| Unit Test | `DigitalPDPTagihanViewModelTest``DigitalPDPTagihanViewModelTestFixture` | We use `Fixture` to contain all mock, validation/assertion logics so it’ll be more readable in the actual test class |
| Instrumentation Test | - | - |

## Useful Links

- [Figma](https://www.figma.com/file/O2ztGn39sOaZFn7kCTFWTR/%5BUI---M%5D-DG-PDP-Revamp-Q4-2021?t=W4vEmNbDVT2exSDY-6)
- [Tracker](https://mynakama.tokopedia.com/datatracker/requestdetail/view/2804)
