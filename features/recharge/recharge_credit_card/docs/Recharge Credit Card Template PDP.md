---
title: "Recharge Credit Card Template PDP"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Misael Jonathan](https://tokopedia.atlassian.net/wiki/people/60051d42e64c95006fbaad73?ref=confluence) [Firmanda Mulyawan Nugroho](https://tokopedia.atlassian.net/wiki/people/5d91c148fdfa560dcc3a040f?ref=confluence) |
| Product Manager | Kevin Winarya, [Hartono Santoso](https://tokopedia.atlassian.net/wiki/people/5c6f1fc4017b4a53c68aa479?ref=confluence) [Alya Ignacia Kam](https://tokopedia.atlassian.net/wiki/people/5d94813bf5d3c10d8bfd4c5f?ref=confluence)  |
| Team | [Minion Mark](https://tokopedia.atlassian.net/people/team/54372146-8afa-46e4-8de3-783c53a0cc3b) |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Module Location | `feature/recharge/recharge_credit_card` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:MA-3.200-->
###### *Fix wrong token on favorite number CC*
<!--end expand-->

<!--start expand:MA-3.196-->
###### *- Added favorite number list for credit card (chip, autocomplete, favnum page)**- Added checkout flow using token (for favorite number purpose)*
<!--end expand-->

## Overview

### Background

A bit introduction on how Recharge products are served in Tokopedia app, we have been using “template” in order to tell our app which page is used to be shown to user. Recharge Credit Card template is one of them, it is a single purpose template for credit card product only. This template has different setup from other templates, from credit card local validation using luhn algorithm modulo, bank/provider list, and confirmation dialog.

### Screenshots



| ![](res/Screen%20Shot%202023-02-20%20at%2013.55.50.png)<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.17.06.png)<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.18.00.png)<br/> |
| --- | --- | --- |

### User Journey



| **User Journey** | **Tech Flow** | **Screenshot** |
| --- | --- | --- |
| User open PDP | [New User] 1. PDP call `rechargeCatalogMenuDetail` and `rechargeBankList`<br/>2. User will be able to see the credit card PDP and its default components<br/><br/>[Returning User]1. PDP call `rechargeCatalogMenuDetail` , `rechargeBankList`, and `digiPersoGetPersonalizedItems` to retrieve basic information and favorite numbers<br/>2. User will be able to see credit card PDP with its default components and additional view:<br/>	1. prefilled number (masked number)<br/>	2. favorite number chips (with favorite number page entry point)<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2013.55.50.png)<br/>![](res/Screen%20Shot%202023-02-20%20at%2014.19.29.png)<br/> |
| User type number in input field | 1. When user type in input field, all the number will be separated into 4-4-4-4 format<br/>2. `rechargeCatalogPrefixSelect` will be called as user type the number, retrieving bank icon and its regex validator. if PDP already has the prefixes data, this gql will not be called so it won’t be redundant<br/>3. PDP show the respective bank icon<br/>4. PDP validate input number with regex in `validations` field from `rechargeCatalogMenuDetail`<br/>5. [Returning User] As user type, the PDP will show autocomplete field which contains all previous transacted cc number (masked).<br/><br/>Additional Notes:1. Token is reset to empty string if user type the number manually<br/>2. Token is reset to empty string if user clear the number using clear icon<br/><br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.27.53.png)<br/> |
| User choose any number from auto complete field | 1. User type number in input field and PDP show the auto complete field<br/>2. User choose any saved number, then it’ll be autofilled to the input field<br/>	1. if the number has custom name, that custom name will be used to replace “Masukkan Nomor Kartu Kredit” text<br/>	2. if the number has no custom name, only the number will be autofilled into the field (masked)<br/>3. PDP show bank icon and validate the number using prefixes from `rechargeCatalogPrefixSelect`<br/><br/>Additional Notes:1. Token is saved in local variable and will be used in addition to the masked client number<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.31.35.png)<br/>![](res/Screen%20Shot%202023-02-20%20at%2014.31.50.png)<br/> |
| User choose any number from chips | 1. User choose any saved number, then it’ll be autofilled to the input field<br/>	1. if the number has custom name, that custom name will be used to replace “Masukkan Nomor Kartu Kredit” text<br/>	2. if the number has no custom name, only the number will be autofilled into the field (masked)<br/>2. PDP show bank icon and validate the number using prefixes from `rechargeCatalogPrefixSelect`<br/>3. Please note that this is a shortcut chips, so there’ll be no highlighted chip<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.31.50.png)<br/> |
| User click favorite number navigation icon | 1. User will be navigated to the favorite number page<br/>2. If user choose any number from this page, it’ll be then autofilled into the PDP input field (same as previous autocomplete & chips)<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.43.32.png)<br/> |
| User click “Lihat Daftar Bank” | 1. Show bank list bottom sheet, with data retrieved from `rechargePCIDSSSignature`<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.46.41.png)<br/> |
| User transact cc product | 1. User click “lanjut” button in PDP<br/>2. PDP show confirmation dialog:<br/>	1. if user click “kembali” then user will be navigated back to PDP<br/>	2. if user click “lanjut”, we’ll call `rechargePCIDSSSignature` to get the signature (for validating the transaction), then send them using `digital/creditcard/iframe`.<br/> | ![](res/Screen%20Shot%202023-02-20%20at%2014.47.46.png)<br/> |

## How-to

### APIs



| **Query Name** | **Type** | **Usage / Notes** | **Cache** |
| --- | --- | --- | --- |
| `rechargeCatalogMenuDetail` | `GQL` | called when user open cc pdp page and when user click `Coba lagi` button (Global error layout). | 5 mins. |
| `rechargeBankList (rechargePCIDSSSignature)` | `GQL` | retrieve bank list | 10 mins. |
| `rechargeCCSignature`(`rechargePCIDSSSignature)` | `GQL` | get signature to validate cc transaction | - |
| `rechargeCatalogPrefixSelect` | `GQL` | retrieve all provider prefixes and validator regex | 10 mins. |
| `digiPersoGetPersonalizedItems` | `GQL` | retrieve favorite numbers in form of prefill, chips, and list | - |
| `digital/creditcard/iframe` | `REST` | submit cc transaction | - |

### Local CC number validation using Luhn

We use luhn to check the credit card number validity (is it a cc number or just a random number). This is different from regex validation that we got from BE.

![](res/Screen%20Shot%202023-02-20%20at%2015.29.27.png)

For further information about Luhn Algorithm you can check this [link](https://www.dcode.fr/luhn-algorithm)

### Favorite Number CC Token Handling

We don’t have the permission show user’s favorite cc number in PDP (security reason), thus all numbers retrieved from perso were masked and we need another way to let user transact using their favorite number. Here we have Token from `digiPersoGetPersonalizedItems`, unique per cc number that we can use to replace the raw number.

Now we have 2 way of transaction:

1. User type number manually and transact cc product using the number
2. User use any saved/favorite numbers and transact using “masked number” and “token”

![](res/Screen%20Shot%202023-02-20%20at%2015.04.36.png)

We have local variable called `token`, and any of these interaction will update the token value:

1. Transact using prefilled number (update token)
2. Choose favorite number from chips, autocomplete, and favorite number page (update token)
3. Type number manually (remove token)
4. Click clear icon (remove token)



---

## Action Items

- Add recommendations and promos section (same as recharge general template)

## Useful Links

- <https://www.figma.com/file/kFrjr5LxZAH75A5sYeovkj/Pay-Credit-Card-Bills---Save-Card-Number?node-id=1451%3A15482>
- Favorite Number trackers:


	- <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2496> (this is Pulsa link, but we use the number 3-8) from this. I couldn’t find the complete thanos link for CC. Will update later.
- [DG Recom - Favorite Number List](/wiki/spaces/BT/pages/1857291703/DG+Recom+-+Favorite+Number+List)
- [[BTRA] Favorite CC Flow](/wiki/spaces/BT/pages/1861751593)
