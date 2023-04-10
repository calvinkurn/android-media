---
title: Entry Point : Add & Edit Address (Source Param)
labels:
---


| **Status** | ​<!--start status:GREEN-->DONE<!--end status--> |
| --- | --- |
| Contributors | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051) |
| Release date | ​12 Aug 2022 / ​<!--start status:GREY-->MA-3.188<!--end status-->  |
| Module type | ​<!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/logistic/logisticaddress` |

<!--toc-->

## Overview

### Background

This document will explain about source param on screen Add address and Edit Address.

### Project Description

Source param serves to find out the entry point that leads to the add/edit address screen and will be used as param in api [add address](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/571965659/Add+Address+-+maps+v1+address+add) and [edit address](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/572194984/Edit+Address+-+maps+v1+address+edit).

## Enum (Entry Point)



```
enum class AddEditAddressSource(val source: String) {
    CHECKOUT("checkout"),
    CART("cart"),
    OCC("occ"),
    TOKOFOOD("tokofood"),
    ADDRESS_LOCALIZATION_WIDGET("addressLocalizationWidget")
}
```



| **Entry Point** | **Source** |
| --- | --- |
| Feature | Description |
| Manage Address | From Manage Address When Add Address | [get source param from manage address](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2034631638/Source+Param+Manage+Address+Entry+Point) |
|  | From Manage Address When Edit Address |
| Tokofood | From Tokofood when don't have an address yet | `tokofood` |
| LCA | From Choose Address Bottom Sheets when don't have an address yet | `addressLocalizationWidget` |
| Checkout | From Checkout when when don't have an address yet | `cart` / ~~checkout~~ |
| OCC | From OCC when don't have an address yet | `occ` |

## How-to

When open add / edit address page we need to put extra `ApplinkConstInternalLogistic.PARAM_SOURCE`



```
val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, sourceValue)
```

### GQL List



| **GQL Name** | **Documentation Link** | **Description** |
| --- | --- | --- |
| `kero_add_address` | [Add Address - /maps/v1/address/add](/wiki/spaces/LG/pages/571965659) | To Add New address |
| `kero_edit_address` | [Edit Address - /maps/v1/address/edit](/wiki/spaces/LG/pages/572194984) | To Edit Address |

