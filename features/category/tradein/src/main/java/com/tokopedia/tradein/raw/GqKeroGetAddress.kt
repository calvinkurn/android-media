package com.tokopedia.tradein.raw

const val GQL_KERO_GET_ADDRESS: String = """query keroAddressCorner(${'$'}input: KeroGetAddressInput){
  keroAddressCorner(input:${'$'}input) {
    status
    config
    server_process_time
    data {
      addr_id
      receiver_name
      addr_name
      address_1
      address_2
      postal_code
      province
      city
      district
      phone
      country
      province_name
      city_name
      district_name
      latitude
      longitude
      status
      is_primary
      is_active
      is_whitelist
      partner_id
      partner_name
      type
      is_corner
    }
    token {
      district_recommendation
      ut
    }
  }
}
"""