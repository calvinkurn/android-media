import { NetworkModule } from 'NativeModules'
import { getEnv, getBaseAPI } from '../lib/util'


export const FETCH_DATA_DIGITAL = 'FETCH_DATA_DIGITAL'
export const fetchDataDigital = (order_id) => {
    console.log(order_id)
    return {
        type: FETCH_DATA_DIGITAL,
        payload: getData(order_id)
    }
}


const getData = async (order_id) => {
    const env = await getEnv()
    const base_api_url = await getBaseAPI(env)
    const dataDigital = await getDataDigital(order_id, base_api_url)
    return dataDigital
}

const getDataDigital = async (order_id, base_api_url) => {
    console.log(base_api_url)
    console.log(order_id)
    const url_digital = `${base_api_url.digital}/v1.4/track/thankyou`
    console.log(url_digital)
    // const url_marketplace = base_api_url.marketplace 
    const payloads = {
        secret_key: 'Tok0p3di4123',
        order_id: order_id
    }

    return NetworkModule.getResponse(url_digital, 'POST', JSON.stringify(payloads), true)
        .then(res => {
            const jsonResponse = JSON.parse(res)
            console.log(jsonResponse)
            return jsonResponse
        })
        .catch(err => {
            console.log(err)
        })
}