import React, { Component } from 'react'
import { StyleSheet, View, Image, TouchableOpacity } from 'react-native'
import { NavigationModule } from 'NativeModules'
import { Text } from '../../common/TKPText'
import { connect } from 'react-redux'
import { fetchShopName } from '../../actions/index'

class Ticker extends Component{
  componentDidMount(){
    this.props.dispatch(fetchShopName())
  }

  render() {
    return (
      <View style={styles.container}>
        <View>
          <View style={styles.imageWrapper}>
          <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/toped.png' }} style={styles.imageIcon} />
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
    marginTop: 20,
    width: '90%'
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