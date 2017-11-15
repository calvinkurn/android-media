import React, { Component } from 'react'
import { StyleSheet, View, Image, TouchableOpacity, DeviceEventEmitter } from 'react-native'
import { NavigationModule } from 'NativeModules'
import { Text } from '../../../common/TKPText'
import { connect } from 'react-redux'
import { fetchShopName } from '../../../actions/index'
import { icons } from '../../../lib/config'


class Ticker extends Component{
  componentDidMount(){
    this.props.dispatch(fetchShopName())

    this.paymentSuccessReloadState =  DeviceEventEmitter.addListener("clearState", (res) => {
      console.log(res)
      if (res){
        this.props.dispatch(fetchShopName())
      }
    })
  }

  componentWillUnmount(){
    this.paymentSuccessReloadState.remove()
  }


  render() {
    return (
      <View style={styles.container}>
        <View>
          <View style={styles.imageWrapper}>
          <Image source={{ uri: icons.toped }} style={styles.imageIcon} />
          </View>
        </View>
        <View style={styles.textContainer}>
          <Text style={{ fontSize: 13 }}>Selamat Datang di {this.props.shopName}</Text>
          <View style={styles.singleLineTextContainer}>
            <Text style={styles.textStyle}>Nikmati Cicilan 0% Gratis Biaya Admin,</Text>
            <TouchableOpacity onPress={() => NavigationModule.navigate('posapp://installment', '')}>
              <Text style={styles.linkText}> Cek Sekarang</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    borderColor: '#cde4c3',
    borderWidth: 1,
    borderLeftWidth: 4,
    backgroundColor: '#fff',
    alignItems: 'center',
    width: '100%'
  },
  textContainer: {
    flexDirection: 'column',
    paddingVertical: 10,
    paddingHorizontal: 10,
  },
  singleLineTextContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  textStyle: {
    fontWeight: 'bold',
    fontSize: 15,
    fontFamily:'Roboto-Medium'
  },
  linkText: {
    color: '#42b549',
    fontWeight: 'bold',
    fontSize: 15,
    fontFamily:'Roboto-Medium'
  },
  imageWrapper: {
    paddingHorizontal: 10
  },
  imageIcon: {
    width: 40,
    height: 30,
    resizeMode: 'cover'
  }
})

const mapStateToProps = (state) => {
  return {
    shopName: state.shop.shopName
  }
}

export default connect(mapStateToProps)(Ticker)