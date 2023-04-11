---
title: Entry Point : Manage Address (Source Param)
labels:
- source
- logistic
- manageaddress
---


| **Status** | ​<!--start status:GREEN-->DONE<!--end status--> |
| --- | --- |
| Contributors | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051) |
| Release date | ​12 Aug 2022 / ​<!--start status:GREY-->MA-3.188<!--end status-->  |
| Module type | ​<!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/logistic/manageaddress` |

<!--toc-->

## Overview

### Background

This document will explain about source param on screen Manage Address.

### Project Description

Source param serves to find out the entry point that leads to the manage address screen and will be used as param in api [get address](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/567411602/Get+Address+-+maps+v1+address).

## Enum (Entry Point)

### Manage Address



```
enum class ManageAddressSource(val source: String) {
    ACCOUNT("account"),
    CART("cart"),
    CHECKOUT("checkout"),
    DISTRICT_NOT_MATCH("districtNotMatch"),
    LOCALIZED_ADDRESS_WIDGET("localizedAddressWidget"),
    NOTIFICATION("notification"),
    TOKOFOOD("tokofood")
}
```



| **Entry Point** | **Source** |
| --- | --- |
| Feature | Description |
| OCC | From OCC when district not match | `districtNotMatch` |
| LCA | From Choose Address Bottom Sheets when already have an address | `localizedAddressWidget` |
| Tokofood | From Tokofood when already have an address | `tokofood` |
| Checkout | From Checkout when Address not matched | `cart` |
| From Checkout when on Change Address | `checkout` |
| Home Account | From Home Account User | `account` |
| Else | From Notification | `notification` |

## How-to

When open add / edit address page we need to put extra `ApplinkConstInternalLogistic.PARAM_SOURCE`



```
val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, sourceValue)
```

## GQL List



| **GQL Name** | **Documentation Link** | **Description** |
| --- | --- | --- |
| `kero_get_address` | [Get Address - /maps/v1/address](/wiki/spaces/LG/pages/567411602) | To Get Address List |

  


