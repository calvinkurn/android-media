package com.tokopedia

const val VALID_QUERY = """
{
  "readme": "Discovery Screen Page level Tracker",
  "mode": "exact",
  "query": [
    {
      "discoveryName": "{{.*}}",
      "currentSite": "tokopediamarketplace"
    },
    {
      "discoveryName": "{{.*}}",
      "currentSite": "tokopediamarketplace",
      "subcategoryId": "{{.*}}",
      "contactInfo": {
        "afUniqueId": "{{.*}}",
        "userSeller": "{{\\d*.\\d*}}"
      }
    }
  ]
}
"""
const val VALID_QUERY_NO_README = """
{
  "mode": "exact",
  "query": [
    {
      "discoveryName": "{{.*}}",
      "currentSite": "tokopediamarketplace"
    },
    {
      "discoveryName": "{{.*}}",
      "currentSite": "tokopediamarketplace",
      "subcategoryId": "{{.*}}",
      "contactInfo": {
        "afUniqueId": "{{.*}}",
        "userSeller": "{{\\d*.\\d*}}"
      }
    }
  ]
}
"""

const val VALID_QUERY_NO_MODE = """
{
  "query": [
    {
      "discoveryName": "{{.*}}",
      "currentSite": "tokopediamarketplace"
    }
  ]
}
"""